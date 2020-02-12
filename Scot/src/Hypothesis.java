import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.utility.Delay;


public class Hypothesis
{ 
   public static void main(String[] args)
    {
      
    	
    
    	
       UltraSonicSensor ultra = new UltraSonicSensor(SensorPort.S4);
       TouchSensor touch1 = new TouchSensor(SensorPort.S1);
       TouchSensor touch2 = new TouchSensor(SensorPort.S3);
       System.out.println("Hypothesis\n");
       System.out.println("Press any key to start");
       
       Button.LEDPattern(4);    // flash green led and 
       Sound.beepSequenceUp();  // make sound when ready.

       Button.waitForAnyPress();
       //Creating gyro
       GyroSensor    gyro = new GyroSensor(SensorPort.S2);
       // create two motor objects to control the motors.
       UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
       UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);

       motorA.setPower(+50);
       motorB.setPower(+50);
       
       
       
       // drive waiting for touch sensor or escape key to stop driving.
       while (Button.ESCAPE.isUp()) 
       {   
    	   Lcd.clear(6);
           Lcd.print(6, "range=%.3f", ultra.getRange());
           Lcd.clear(5);
           Lcd.print(5, "angle=%d av=%.3f", gyro.getAngle(), gyro.getAngularVelocity());
           float inf_Test = ultra.getRange();
           // watch for obstacle.
           if ((ultra.getRange() <= .20) && ((!touch1.isTouched())||(!touch2.isTouched())))
           {
        	// Set gyro angle to zero.
               gyro.reset();

               Lcd.clear(7);
               Lcd.print(7, "angle=%d", gyro.getAngle());
               Delay.msDelay(50);

               // start rotation around current location.
               motorA.setPower(-50);
               motorB.setPower(+50);
               
               // wait for 90 degrees of rotation
               while (Math.abs(gyro.getAngle()) < 90  && Button.ESCAPE.isUp())
               {
                   Lcd.clear(7);
                   Lcd.print(7, "angle=%d", gyro.getAngle());
                   Delay.msDelay(50);
               }

               // back to straight driving.
               motorA.setPower(+50);
               motorB.setPower(+50);
           }
           //Detecting collision
           else if ((touch1.isTouched())||(touch2.isTouched()))
           {
        	   // start rotation around current location.
               motorA.setPower(-50);
               motorB.setPower(-50);
               Delay.msDelay(400); 
               
               gyro.reset();
            // start rotation around current location.
               motorA.setPower(-50);
               motorB.setPower(+50);
            // wait for 90 degrees of rotation
               while (Math.abs(gyro.getAngle()) < 90  && Button.ESCAPE.isUp())
               {
                   Lcd.clear(7);
                   Lcd.print(7, "angle=%d", gyro.getAngle());
                   Delay.msDelay(50);
               }
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
       gyro.close();
       touch1.close();
       touch2.close();
       
       Sound.beepSequence(); // we are done.
   }
 }