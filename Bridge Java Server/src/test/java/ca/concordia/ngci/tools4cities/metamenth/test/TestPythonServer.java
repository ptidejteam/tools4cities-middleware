package ca.concordia.ngci.tools4cities.metamenth.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.IOException;

public class TestPythonServer {

    private Process process = null;
   
    
    @Before
    public void setUp() throws IOException{
        String pythonScriptPath = "Bridge Python Server/metamenth/server.py";
        // Code to set up the test environment
        ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath);
        // Redirect error stream
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
        
    }

    @After
    public void tearDown() {
        if (process != null) {
            // Stop the Python process
            process.destroy();
            
        }
    }

    @Test
    public void testCreateBinaryMeasure(){
        Assert.assertEquals(1, 1);
    }
}
