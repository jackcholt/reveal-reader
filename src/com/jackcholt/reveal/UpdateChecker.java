package com.jackcholt.reveal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flurry.android.FlurryAgent;

import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Checks for updates to the program
 * 
 * by Dave Packham
 */

public class UpdateChecker {
  public static String marketId;
  

  @SuppressWarnings("finally")
  public static int getLatestVersionCode(Context _this) {
    int version = 0;
    try {
      //Get the XML update Version to prompt user to get a new Update From the market
      FlurryAgent.onEvent("UpdateCheck");
      URLConnection cnVersion;
      URL urlVersion = new URL("http://revealreader.thepackhams.com/revealVersion.xml?ClientVer=" + Global.SVN_VERSION);
      cnVersion = urlVersion.openConnection();
      cnVersion.setReadTimeout(10000);
      cnVersion.setConnectTimeout(10000);
      cnVersion.setDefaultUseCaches(false);
      cnVersion.connect();
      InputStream streamVersion = cnVersion.getInputStream();
  	  //Get the Update location URL
      WebView wv = new WebView(_this);
      wv.clearCache(true);
      wv.getSettings().setJavaScriptEnabled(true);
      wv.loadUrl("http://revealreader.thepackhams.com/revealUpdate.html");
     //Proceed parsing the info
      DocumentBuilder docBuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document manifestDoc = docBuild.parse(streamVersion);
      NodeList manifestNodeList = manifestDoc.getElementsByTagName("manifest");
      String versionStr =
          manifestNodeList.item(0).getAttributes().getNamedItem("android:versionCode")
              .getNodeValue();
      version = Integer.parseInt(versionStr);
      NodeList clcNodeList = manifestDoc.getElementsByTagName("clc");
      marketId = clcNodeList.item(0).getAttributes().getNamedItem("marketId").getNodeValue();
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (FactoryConfigurationError e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
    	Global.NEW_VERSION = version;
      return version;
    }
  }

  public static  void checkForNewerVersion(int currentVersion, Context _this) {
	  //Check here an XML file stored on our website for new version info
	  //Comment this out due to feedback fromusers that it checks too much.  at least that it notifies too much
      //Toast.makeText(_this, R.string.checking_for_new_version_online, Toast.LENGTH_SHORT).show();
  	  Global.NEW_VERSION = getLatestVersionCode(_this);
    if (Global.NEW_VERSION > currentVersion) {
    	//Tell user to Download a new REV of this cool CODE from the Market
    	//Only Toast if there IS and update
        Toast.makeText(_this, R.string.update_available, Toast.LENGTH_LONG).show();
    }
  }




}
