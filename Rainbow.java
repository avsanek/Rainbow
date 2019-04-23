package Lastver;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.*;
import robocode.util.Utils;
/**
* @autor Shcharbakow Aliaksandr
*/
public class Rainbow extends AdvancedRobot {
   
    final static double SHOT=12;
    final static double SHOT_SPEED=11;

    static double way=1;
    static double oldEnemyPoz;
    static double enemyPower;

    public void run(){
        setBodyColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        setGunColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        setRadarColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        setBulletColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        setScanColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
    }
    public void onScannedRobot(ScannedRobotEvent e){
	if(e.getTime() % 16 == 0) {
            setBodyColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
            setGunColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
            setRadarColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
            setBulletColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
            setScanColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
			}

        double moduleBearing=e.getBearingRadians()+getHeadingRadians();       
        double rotate=moduleBearing+Math.PI/2;
        rotate-=Math.max(0.5,(1/e.getDistance())*100)*way;
        setTurnRightRadians(Utils.normalRelativeAngle(rotate-getHeadingRadians()));

        if(enemyPower>(enemyPower=e.getEnergy())){
            if(Math.random()>300/e.getDistance()){
                way=-way;
            }
        }

        setMaxVelocity(400/getTurnRemaining());
        setAhead(100*way); 

        double enemyPoz = e.getHeadingRadians();
        double enemyPozChange = enemyPoz - oldEnemyPoz;
        oldEnemyPoz = enemyPoz;

        double xyTime = 0;
        double foretoldX = getX()+e.getDistance()*Math.sin(moduleBearing);
        double foretoldY = getY()+e.getDistance()*Math.cos(moduleBearing);
        while((++xyTime) * SHOT_SPEED <  Point2D.Double.distance(getX(), getY(), foretoldX, foretoldY)){ 

            foretoldX += Math.sin(enemyPoz) * e.getVelocity();
            foretoldY += Math.cos(enemyPoz) * e.getVelocity(); 

            enemyPoz += enemyPozChange;
            
            foretoldX=Math.max(Math.min(foretoldX,getBattleFieldWidth()-18),18);
            foretoldY=Math.max(Math.min(foretoldY,getBattleFieldHeight()-18),18);
        }     
        double target = Utils.normalAbsoluteAngle(Math.atan2(  foretoldX - getX(), foretoldY - getY()));      
        setTurnGunRightRadians(Utils.normalRelativeAngle(target - getGunHeadingRadians()));
	    double DAMAGE = Math.min(3.0, getEnergy());
        setFire(DAMAGE);
        setTurnRadarRightRadians(Utils.normalRelativeAngle(moduleBearing-getRadarHeadingRadians())*2);
    }
    public void onBulletHit(BulletHitEvent e){
        enemyPower-=SHOT;
    }
    public void onHitWall(HitWallEvent e){
        way=-way;
    }
    public void onWin(WinEvent event){
            turnRight(10);
        while(true) {
            turnLeft(35);
            turnRight(35);
        }
    }
}
