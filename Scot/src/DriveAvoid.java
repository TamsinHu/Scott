import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.utility.Delay;


public class DriveAvoid 
{ 
    public static void main(String[] args)
    {
      
    	
    
    	
       UltraSonicSensor ultra = new UltraSonicSensor(SensorPort.S4);
       
        
       System.out.println("Drive and Avoid\n");
       System.out.println("Press any key to start");
       
       Button.LEDPattern(4);    // flash green led and 
       Sound.beepSequenceUp();  // make sound when ready.

       Button.waitForAnyPress();
        
       // create two motor objects to control the motors.
       UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
       UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);

       motorA.setPower(+50);
       motorB.setPower(+50);
       //Delay.msDelay(2000);
       
       
       // drive waiting for touch sensor or escape key to stop driving.
       while (Button.ESCAPE.isUp()) 
       {   
    	   Lcd.clear(6);
           Lcd.print(6, "range=%.3f", ultra.getRange());

           // watch for obstacle.
           if (ultra.getRange() < .25)
           {
        	  
               Delay.msDelay(50);

               // start rotation around current location.
               motorA.setPower(-50);
               motorB.setPower(+50);
               Delay.msDelay(50);
              

               // back to straight driving.
               motorA.setPower(+50);
               motorB.setPower(+50);
           }
           
           Delay.msDelay(50);
       }
       
       // stop motors with brakes on.
       motorA.stop();
       motorB.stop();

       // free up resources.
       motorA.close();
       motorB.close();
       ultra.close();
       
       Sound.beepSequence(); // we are done.
   }
 }
