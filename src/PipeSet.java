import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class PipeSet {
    private final Image PIPE_IMAGE;
    private final int PIPE_GAP = 168;
    private double pipeSpeed = 5;
    private final double START = 100;
    private final double END = 500;
    private final double TOP_PIPE_Y;
    private final double BOTTOM_PIPE_Y;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private final double RANDOM_POSITION;
    private ArrayList<Integer> pipeTypes = new ArrayList<>();
    private boolean levelUp;
    private boolean isCollide = false;
    private boolean isPass = false;


    public PipeSet(Image pipeImage, boolean isLevelUp){
        levelUp = isLevelUp;
        PIPE_IMAGE = pipeImage;
        if (!levelUp){
            pipeTypes.add(100);
            pipeTypes.add(300);
            pipeTypes.add(500);
            Random rand = new Random();
            RANDOM_POSITION = pipeTypes.get(rand.nextInt(pipeTypes.size()));
        }else{
            double rand = new Random().nextDouble();
            RANDOM_POSITION = START + (rand *(END - START));
        }
        TOP_PIPE_Y = RANDOM_POSITION - PIPE_IMAGE.getHeight()/2;
        BOTTOM_PIPE_Y = PIPE_IMAGE.getHeight()/2 + RANDOM_POSITION + PIPE_GAP;

    }

    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }

    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));

    }
    public boolean getIsPass(){
        return this.isPass;
    }
    public void setIsPass(boolean isPass){
        this.isPass = isPass;
    }

    public void updateSpeed(int timeScale){
        pipeSpeed = 5;
        for (int i = 1; i < timeScale; i++){
            pipeSpeed = pipeSpeed * 1.5;
        }
        pipeX -= pipeSpeed;
    }

    public void setIsCollide(boolean isCollide){
        this.isCollide = isCollide;
    }
    public boolean getIsCollide(){
        return this.isCollide;
    }
    public void renderPipeSet(){
        if(!isCollide){
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
    }
    public void update(int timeScale){
        renderPipeSet();
        updateSpeed(timeScale);
    }

    public Image getPipeImage(){
        return this.PIPE_IMAGE;
    }

    public double getPipeX() {
        return pipeX;
    }

    public double getPipeY(){
        return this.TOP_PIPE_Y;
    }

    public double getBOTTOM_PIPE_Y(){
        return this.BOTTOM_PIPE_Y;
    }

    public DrawOptions getRotator(){
        return this.ROTATOR;
    }
}
