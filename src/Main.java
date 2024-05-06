import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, String> items = new ConcurrentHashMap<>();


        GameStatus gameStatus = new GameStatus();
        GameConfig gameConfig = new GameConfig();
        gameConfig.setUnfairGame(false);

        Thread task = new Thread() {
            int powerPositionRandom = 0;
            @Override
            public void run() {
                while(!GameStatus.isWinners.get()){
                    powerPositionRandom = (int)Math.floor(Math.random()*1000)+1;
                    items.clear();
                    // Actualizar el mapa con nuevos datos
                    items.put(powerPositionRandom, "Power");
                    // Imprimir el mapa actualizado
                    System.out.println("******** --> POWER IN POSITION: " + powerPositionRandom + "<-- *********");
                    gameStatus.setItems(items);
                    try {
                        sleep(10*1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        };
        ExecutorService executorPower = Executors.newCachedThreadPool();
       executorPower.submit(task);

        ExecutorService executorService = Executors.newCachedThreadPool();
            int velocity = (int)Math.floor(Math.random()*3)+1;
            int resistance = (int)Math.floor(Math.random()*3)+1;
            for (int i = 0; i < GameConfig.numberHorses; i++) {
                HorseRunner horseRunner = new HorseRunner("horse "+i, velocity, resistance, gameStatus);
                GameStatus.horses.add(horseRunner);
                executorService.submit(horseRunner);

            }




        executorService.shutdown();
        executorPower.shutdown();

    }
}

