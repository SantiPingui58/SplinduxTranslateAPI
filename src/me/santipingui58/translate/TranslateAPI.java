package me.santipingui58.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.scheduler.BukkitRunnable;
import org.jsoup.Jsoup;

public class TranslateAPI {
	
	private static TranslateAPI manager;	
	 public static TranslateAPI getAPI() {
	        if (manager == null)
	        	manager = new TranslateAPI();
	        return manager;
	    }

	 
	 
	 public List<String> urlList = new ArrayList<String>();
	 
	 private String selectedURL;
	 
	public CompletableFuture<String> translate(Language langFrom, Language langTo, String text) throws IOException {
		CompletableFuture<String> result = new CompletableFuture<>();
		String langF = langFrom ==null ? "" : "&source="+langFrom.toGoogleType();
		if (langFrom!=null && langFrom.equals(langTo))  {
			result.complete(text);
			return result;
		}
		new BukkitRunnable() {public void run() {
			
				if (text.startsWith("/")) result.complete(text);
				if (selectedURL==null) selectedURL = "https://script.google.com/macros/s/"+urlList.get(0) + "/exec";
				try {
		        String urlStr = selectedURL +
		                "?q=" + URLEncoder.encode(text, "UTF-8") +
		                "&target=" + langTo.toGoogleType() +
		                langF;
		        URL url = new URL(urlStr);
		        StringBuilder response = new StringBuilder();
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		        con.setRequestProperty("User-Agent", "Mozilla/5.0");
		        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) {
		            response.append(inputLine);
		        }
		        in.close();
		        if (response.toString().contains("<!DOCTYPE")) {
		        changeURL();	      
		        } else {
		        result.complete(Jsoup.parse(response.toString()).text());
		        }
		        
				} catch(Exception ex) {
					ex.printStackTrace();
					result.complete(text);
				}
				
			}}.runTaskAsynchronously(Main.get());
		  return result;
	    }
	

	
	  
	  public void changeURL() {
		  
		  //0
		  int index = urlList.indexOf(selectedURL);
		       //1          //2
		  if (index+1>=urlList.size()) {
			  index=-1;
		  } 
		  this.selectedURL = urlList.get(index+1);
	  }
	
	
	
}
