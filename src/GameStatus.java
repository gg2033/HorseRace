import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameStatus {
    public static volatile AtomicBoolean isWinners = new AtomicBoolean(false);
    public static volatile AtomicBoolean isShowPostion = new AtomicBoolean(false);

    public static Map<Integer, String> items = new ConcurrentHashMap<>();
    public static Map<String, Integer> positions = new ConcurrentHashMap<>();
    public static Semaphore raceFinish = new Semaphore(3);
    public static List<HorseRunner> horses = new ArrayList<>();
    public static synchronized AtomicBoolean getIsWinner() {
        return isWinners;
    }
    public synchronized static void setIsWinner(Boolean isWinner) {
        GameStatus.isWinners = new AtomicBoolean(isWinner);
    }

    public synchronized Map<Integer, String> getItems() {
        return items;
    }

    public synchronized void setItems(Map<Integer, String> items) {
        this.items = items;
    }

    public synchronized Map<String, Integer> getPositions() {
        return positions;
    }


    public  static synchronized void showPositions(String name, int position, HorseRunner horseRunner){

            if(!isWinners.get()){
                if(position<=1000 ){
                    System.out.printf("%s. Position : %s \n", name, position);
                }else{

                    System.out.printf("Winner is %s, Position %s CONGRATULATION \n", name, position);
                        horseRunner.setWinner(true);
                    try {
                        GameStatus.raceFinish.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    }
                }
            }

    public static synchronized void showPower(String name){
        if(!isWinners.get()){
            System.out.printf("*** %s. With Power :D ** \n", name);
        }
    }

    public static synchronized void checkWinners(HorseRunner horseRunner){
        if (horseRunner.isWinner()) {
            if (GameStatus.raceFinish.availablePermits()==0) {
                GameStatus.isWinners.set(true);
            }
        }
    }

    public static synchronized void showEndingPositions(){
        if(GameStatus.raceFinish.availablePermits() ==0 && !isShowPostion.get()){
            for (HorseRunner horse :
                    GameStatus.horses) {
                System.out.printf(horse.getNameHorse()  + " Ending Position: "+horse.position+"\n");
            }
            isShowPostion.set(true);
        }
    }
}
