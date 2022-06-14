import bagel.Image;

public class LifeBar {
    private final Image FULL_HEART = new Image("res/level/fullLife.png");
    private final Image EMPTY_HEART = new Image("res/level/noLife.png");
    private final int HEART_INITIAL_X = 120;
    private final int HEART_INTERVAL_X = 50;
    private final int HEART_Y = 50;
    private int maxLife;
    private int currentLife;

    public LifeBar(){
        maxLife = 3;
        currentLife = 3;
    }
    public int getCurrentLife(){
        return this.currentLife;
    }
    public void setCurrentLife(int currentLife){
        this.currentLife = currentLife;
    }
    public void setMaxLife(int maxLife){
        this.maxLife = maxLife;
    }
    public void update(){
        renderLifeBar();
    }

    public void renderLifeBar(){
        for(int i = 0; i < currentLife; i++){
            FULL_HEART.draw(HEART_INITIAL_X + i * HEART_INTERVAL_X, HEART_Y);
        }
        for (int j = currentLife; j < maxLife; j++){
            EMPTY_HEART.draw(HEART_INITIAL_X + j * HEART_INTERVAL_X, HEART_Y);
        }
    }

}
