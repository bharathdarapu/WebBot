package wm.morganstanley.advisory.cloudathon.dis;

	import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
	import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

	public class SpeechRecognition {

		
		private static String speechSubscriptionKey = "de9d1aa5d4c64b38b98f0fc121279396";
		private static String serviceRegion = "eastus";
		
		 public static void main(String[] args) throws InterruptedException, ExecutionException {
		        SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
		        singleWordSpeechRecognition(speechConfig);
		    }
	    
	    	 public static void singleWordSpeechRecognition(SpeechConfig speechConfig) throws InterruptedException, ExecutionException {
	    	        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
	    	        SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

	    	        System.out.println("Speak into your microphone.");
	    	        Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
	    	        SpeechRecognitionResult result = task.get();
	    	        System.out.println("RECOGNIZED: Text=" + result.getText());
	    	    }
	    	
	    }
	    