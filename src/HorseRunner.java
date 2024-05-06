import java.util.Objects;

public class HorseRunner extends Thread {
    private final String name;
    private final int velocity;
    private final int resistence;
    public int position = 0;
    private int priority=5;
    private int executions=0;
    private  volatile  boolean isWinner = false;
    private GameStatus gameStatus;

    public HorseRunner(String name, int velocity, int resistence,  GameStatus gameStatus){
        this.name=name;
        this.velocity=velocity;
        this.resistence=resistence;
        this.gameStatus = gameStatus;
    }
    @Override
    public void run() {

                while(!GameStatus.getIsWinner().get() && !this.isWinner){
                        simulateProgress();
                }
        GameStatus.showEndingPositions();

    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public void simulateProgress(){

        if(this.isPositionWithPower()){
            GameStatus.showPower(name);
            loadPower();
        }else{
            this.position += moveFordward();
        }
        GameStatus.showPositions(name, position, this);
        GameStatus.checkWinners(this);
        this.horseWait(this);

    }

    private synchronized int moveFordward(){
        return Math.multiplyExact(this.velocity, (int)Math.floor(Math.random()*10)+1);
    }

    private void  horseWait(HorseRunner horseRunner) {

            if(!gameStatus.getIsWinner().get()){
                int timeWait = (int)Math.floor(Math.random()*5)+1;
                timeWait = timeWait - this.resistence;

                if(timeWait<0){
                    timeWait = 1;
                }
                priority = priority+(-2*executions);
                if(priority<1){
                    executions=0;
                    priority=10;
                }
                this.setPriority(priority);
                System.out.printf("%s. Wait: %s \n", this.getNameHorse(), timeWait);
                try {

                       Thread.sleep(timeWait*100);


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

    }
    public synchronized  static void Slepping(HorseRunner runner){

    }

    public synchronized boolean isPositionWithPower(){
        if(!Objects.isNull(GameStatus.items.get(position)) && GameStatus.items.get(position).equals("Power")){
            return true;
        }
        return false;
    }
    public int getPositionHorse(){
        return gameStatus.getPositions().get(name);
    }

    public void loadPower(){
        this.position = this.position + 50;
    }


    public String getNameHorse(){
        return this.name;
    }

}
