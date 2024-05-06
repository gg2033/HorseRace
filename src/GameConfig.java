public class GameConfig {
    private static boolean isUnfairGame = false;
    //Number of horses
    public static final int numberHorses = Runtime.getRuntime().availableProcessors();

    public static boolean isUnfairGame() {
        return isUnfairGame;
    }

    public static void setUnfairGame(boolean unfairGame) {
        isUnfairGame = unfairGame;
    }
}
