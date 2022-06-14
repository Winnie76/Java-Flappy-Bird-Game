import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SteelPipeSet extends PipeSet{
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final double SWITCH_FLAME = 20;
    private int frameCount = 0;
    private int flameDuration = 0;
    private boolean collideWithFlame = false;

    public SteelPipeSet(Image steel_pipe, boolean isLevelUp) {
        super(steel_pipe, isLevelUp);
    }
    public void update(int timeScale){
        frameCount += 1;
        if(frameCount % 20 == 0){
            if(!super.getIsCollide() && flameDuration < 30){
                shootFlame();
                frameCount -= 1;
                flameDuration += 1;
            }
        }
        if(flameDuration == 30){
            flameDuration = 0;
            frameCount += 30;
        }
        super.update(timeScale);
    }
    public void shootFlame(){
        FLAME_IMAGE.draw(super.getPipeX(), super.getPipeY() + Window.getHeight()/2.0 + 10);
        FLAME_IMAGE.draw(super.getPipeX(), super.getBOTTOM_PIPE_Y() - Window.getHeight()/2.0 - 10, super.getRotator());

    }
    public Rectangle getTopFlame(){
        return FLAME_IMAGE.getBoundingBoxAt(new Point(super.getPipeX(), super.getPipeY()));
    }

    public Rectangle getBottomFlame(){
        return FLAME_IMAGE.getBoundingBoxAt(new Point(super.getPipeX(), super.getBOTTOM_PIPE_Y()));
    }

    public int getFrameCount(){
        return this.frameCount;
    }

    public boolean getCollisionWithFlame(){
        return this.collideWithFlame;
    }

    public void setCollideWithFlame(boolean collideWithFlame){
        this.collideWithFlame = collideWithFlame;
    }

}
