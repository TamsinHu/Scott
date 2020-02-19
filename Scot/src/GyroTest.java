import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class GyroTest {
	public static void main(String[] args)
    {
       UltraSonicSensor ultra = new UltraSonicSensor(SensorPort.S4);
       TouchSensor touch1 = new TouchSensor(SensorPort.S1);
       TouchSensor touch2 = new TouchSensor(SensorPort.S3);
       System.out.println("GyroTest\n");
       System.out.println("Press any key to start");
       
       Button.LEDPattern(4);    // flash green led and 
       Sound.beepSequenceUp();  // make sound when ready.

       Button.waitForAnyPress();
       //Creating gyro
       GyroSensor    gyro = new GyroSensor(SensorPort.S2);
       // create two motor objects to control the motors.
       UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
       UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);

       motorA.setPower(+100);
       motorB.setPower(+100);
       
       
       
       // drive waiting for touch sensor or escape key to stop driving.
       while (Button.ESCAPE.isUp()) 
       {   
    	   Lcd.clear(6);
           Lcd.print(6, "range=%.3f", ultra.getRange());
           Lcd.clear(5);
           Lcd.print(5, "angle=%d av=%.3f", gyro.getAngle(), gyro.getAngularVelocity());
           // watch for obstacle.
           if ((ultra.getRange() <= .15) && ((!touch1.isTouched())||(!touch2.isTouched())))
           {
        	   
               sense(gyro, motorA, motorB, ultra);
               
           }
           
           //Detecting collision
           else if ((touch1.isTouched())||(touch2.isTouched()))
           {
        	   // Reverse
               motorA.setPower(-100);
               motorB.setPower(-100);
               Delay.msDelay(600); 
               
               sense(gyro, motorA, motorB, ultra);
           }
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
	
	public static void sense(GyroSensor gyro, UnregulatedMotor motorA, UnregulatedMotor motorB, UltraSonicSensor ultra) {
		motorA.setPower(0);
        motorB.setPower(0);
        Delay.msDelay(500);
        // Set gyro angle to zero.
        gyro.reset();
        
        int angle = 80;
        
        float distance1;
        float distance2;

        Lcd.clear(7);
        Lcd.print(7, "angle=%d", gyro.getAngle());

        // start rotation around current location.
        motorA.setPower(-100);
        motorB.setPower(+100);
        
        // wait for 90 degrees of rotation
        while (Math.abs(gyro.getAngle()) < angle  && Button.ESCAPE.isUp())
        {
            Lcd.clear(7);
            Lcd.print(7, "angle=%d", gyro.getAngle());
        }
        motorB.setPower(0);
        motorA.setPower(0);
        Lcd.clear(2);
        Lcd.print(2, "angle=%d", gyro.getAngle());
        Delay.msDelay(50);
        distance1 = ultra.getRange();
        Delay.msDelay(50);

        Lcd.clear(7);
        Lcd.print(7, "angle=%d", gyro.getAngle());
        // start rotation around current location.
        motorA.setPower(+100);
        motorB.setPower(-100);
        Delay.msDelay(500);
        
        // wait for 90 degrees of rotation
        while (Math.abs(gyro.getAngle()) < angle  && Button.ESCAPE.isUp())
        {
            Lcd.clear(7);
            Lcd.print(7, "angle=%d", gyro.getAngle());
        }
        motorB.setPower(0);
        motorA.setPower(0);
        Delay.msDelay(50);
        distance2 = ultra.getRange();

        
        if (distance2>distance1) {
     	   
        }
        else {
            Lcd.clear(7);
            Lcd.print(7, "angle=%d", gyro.getAngle());
            // start rotation around current location.
            motorA.setPower(-100);
            motorB.setPower(+100);
            Delay.msDelay(500);
            
            // wait for 90 degrees of rotation
            while (Math.abs(gyro.getAngle()) < angle  && Button.ESCAPE.isUp())
            {
                Lcd.clear(7);
                Lcd.print(7, "angle=%d", gyro.getAngle());
            }
        }
        

        // back to straight driving.
        motorA.setPower(+100);
        motorB.setPower(+100);
	}
}
