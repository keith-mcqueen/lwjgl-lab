package CS355.LWJGL;

import CS355.mcqueen.keith.CameraController;

/**
 *
 * @author Brennan Smith
 */
public class CS355LWJGL 
{
    
    public static void main(String[] args) 
  {
    LWJGLSandbox main = null;
    try 
    {
      main = new LWJGLSandbox();
      main.create(new CameraController());
      main.run();
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    finally {
      if(main != null) 
      {
        main.destroy();
      }
    }
  }
    
}
