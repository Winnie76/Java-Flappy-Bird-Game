import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Random;

public class Weapon {
    private double weaponSpeed = 5;
    private double weaponX = Window.getWidth();
    private boolean isPickedUp = false;
    private boolean isShoot = false;
    private boolean disappear = false;
    private boolean overlap = false;
    private int distance = 0;
    private final double START = 100;
    private final double END = 500;

    private double weaponY;
    private final Image WEAPON_IMAGE;
    private final double RANDOM_POSITION;

    public Weapon(Image weaponImage){
        WEAPON_IMAGE = weaponImage;
        double rand = new Random().nextDouble();
        RANDOM_POSITION = START + (rand *(END - START));
        weaponY = RANDOM_POSITION;
    }
    public void update(Input input, Bird bird, int timeScale){
        updateSpeed(timeScale);
        if(!isPickedUp){
            this.weaponX -= weaponSpeed;
        }else{
            if(bird.getHoldWeapon() && !isShoot){
                if(input.wasPressed(Keys.S)){
                    bird.setHoldWeapon(false);
                    this.isShoot = true;
                }
                setWeaponX(bird.getBox().right());
                setWeaponY(bird.getY());
            }
        }

    }
    public void updateSpeed(int timeScale){
        weaponSpeed = 5;
        for(int i = 1; i < timeScale; i++){
            weaponSpeed = weaponSpeed * 1.5;
        }
    }

    public void renderWeapon(int shootRange){
        if(isShoot){
            if(distance <= shootRange){
                this.weaponX += 5;
                distance += 5;
            }else{
                this.disappear = true;
            }
        }
        if(!disappear && !overlap){
            WEAPON_IMAGE.draw(weaponX, weaponY);
        }
    }

    public void setWeaponX(double right){
        this.weaponX = right;
    }

    public void setWeaponY(double weaponY){
        this.weaponY = weaponY;
    }

    public Image getWeaponImage(){
        return this.WEAPON_IMAGE;
    }

    public Rectangle getBox(){
        return WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
    }

    public boolean getDisappear(){
        return this.disappear;
    }

    public void setDisappear(boolean disappear){
        this.disappear = disappear;
    }

    public boolean getIsShoot(){
        return this.isShoot;
    }

    public boolean getIsPickedUp(){
        return this.isPickedUp;
    }

    public void setIsPickedUp(boolean isPickedUp){
        this.isPickedUp = isPickedUp;
    }

    public boolean getOverlap(){
        return this.overlap;
    }

    public void setOverlap(boolean overlap){
        this.overlap = overlap;
    }

}
