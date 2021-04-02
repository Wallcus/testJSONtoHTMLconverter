package converter_package;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.Iterator;


public class converterClass
{
	public static String parseJSONObjectRecursiveTag(JSONObject jsonObj, int lvl)
	{
		String tab = "";
		
		for(int i = 0; i < lvl; i++)
		{
			tab += "\t";
		}
		
		Iterator<String> keys = jsonObj.keySet().iterator();
		
		String kHTML = "";
		
		String kTagAttributes = "";
		
		String kContent = "";
		
		String temp = "";
		{
			for(int k = 0; k < jsonObj.size() && keys.hasNext(); k++)
			{
				temp = keys.next();
				if(temp.compareTo("attributes") == 0)
				{
					//send as attribute, because it can return 1 string that is inserted into the tag
					kTagAttributes += parseJSONObjectRecursiveAttribute((JSONObject)jsonObj.get(temp));
				}
				else if (jsonObj.get(temp) instanceof JSONObject)
				{
					kContent += "\n" + tab + "<" + temp + ">" + parseJSONObjectRecursiveTag((JSONObject)jsonObj.get(temp), lvl+1) + "\n" + tab + "</" + temp + ">";
				}
				else if(jsonObj.get(temp) instanceof String) 
				{
					kContent += "\n" + tab + "<" + temp + ">" + jsonObj.get(temp) + "</" + temp + ">";
				}
				//in this level of iteration, assume remaining objects are custom tags
			}
		}
		
		
		kHTML += kTagAttributes;
		
		kHTML += kContent;
		
		kHTML += "" ;
		
		//System.out.println(kHTML);
		
		return kHTML;
	}
	
	public static String parseJSONObjectRecursiveAttribute(JSONObject jsonObj)
	{
		Iterator<String> keys = jsonObj.keySet().iterator();
		
		String kHTML = "";
		
		for(int k = 0; k < jsonObj.size(); k++)
		{
			String temp = keys.next();
		
			if(temp.compareTo("language") == 0)
			//due to example files, unique rule for "language", as it has to be "lang" in output html. If input is lang already, it is normally handled as an attribute in the next else if
			{
				kHTML += (" lang" + "=\"" + jsonObj.get(temp) + "\"");
			}
			else if(temp.compareTo("style") == 0)
			{
				kHTML += " " + temp + "=\"" + parseJSONObjectRecursiveStyle((JSONObject)jsonObj.get(temp)) + "\"";
			}
			else if(jsonObj.get(temp) instanceof String)
			{
				kHTML += " " + temp + "=\"" + jsonObj.get(temp) + "\"";
				
			}
			else
			{
				//shouldnt get here
			}
		}
		
		return kHTML + ">";
	}
	public static String parseJSONObjectRecursiveStyle(JSONObject jsonObj)
	{
		Iterator<String> keys = jsonObj.keySet().iterator();
		
		String tempHTML = "";
		
		for(int i = 0; i < jsonObj.size(); i++)
		{
			String temp = keys.next();
			
			tempHTML += temp + ":" + jsonObj.get(temp) + ";";
		}
		
		return tempHTML;
	}
	public static String parseJSONObjectRecursiveHead(JSONObject jsonObj, int lvl)
	{
		String tab = "";
		
		for(int i = 0; i < lvl; i++)
		{
			tab += "\t";
		}
		
		Iterator<String> keys = jsonObj.keySet().iterator();
			
		String kHTML = "";
		
		String temp = "";
		
		for(int k = 0; k < jsonObj.size(); k++)
		{
			temp = keys.next();
			if(jsonObj.get(temp) instanceof JSONObject)
			{
				if(temp.compareTo("meta") == 0)
				{
					kHTML += parseJSONObjectRecursiveMeta((JSONObject)jsonObj.get(temp), lvl);
				}
				else
				{
					kHTML += "\n" + tab + "<" + temp + ">" + parseJSONObjectRecursiveTag((JSONObject)jsonObj.get(temp), lvl) + "\n" + tab + "</" + temp + ">";
				}
			}
			//NOT IMPLEMENTED
			else if(temp.compareTo("link") == 0)
			{
				//kHTML += " " + temp + "=\"" + parseJSONObjectRecursiveLink((JSONObject)jsonObj.get(temp), lvl) + "\"";
			}
			else if(jsonObj.get(temp) instanceof String)
			{
				kHTML += "\n" + tab + "<" + temp + ">" + "=\"" + jsonObj.get(temp) + "\"" + "</" + temp + ">";
				
			}
		}
		
		return kHTML;
	}
	/*
	public static String parseJSONObjectRecursiveLink(JSONObject jsonObj, int lvl)
	{
		String tab = "";
		
		for(int i = 0; i < lvl; i++)
		{
			tab += "\t";
		}
		
		Iterator<String> keys = jsonObj.keySet().iterator();
		
		String tempHTML = "";
		
		String temp = "";
		
		for(int i = 0; i < jsonObj.size(); i++)
		{
			temp = keys.next();
			
			tempHTML += parseJSONObjectRecursiveLinkIndividual((JSONObject)jsonObj.get(temp), lvl);
		}
		
		return tempHTML;
	}
	
	public static String parseJSONObjectRecursiveLinkIndividual(JSONObject jsonObj, int lvl)
	{
		String tab = "";
		
		for(int i = 0; i < lvl; i++)
		{
			tab += "\t";
		}
		
		Iterator<String> keys = jsonObj.keySet().iterator();
		
		String tempHTML = "";
		
		String temp = "";
		
		for(int i = 0; i < jsonObj.size(); i++)
		{
			temp = keys.next();
			
			tempHTML += "\n" + tab + "<link " + "name=\"" + temp + "\" " + "content=\"" + jsonObj.get(temp) + "\">";

		}
		
		return tempHTML;
	}
	*/
	public static String parseJSONObjectRecursiveMeta(JSONObject jsonObj, int lvl)
	{
		String tab = "";
		
		for(int i = 0; i < lvl; i++)
		{
			tab += "\t";
		}
		
		Iterator<String> keys = jsonObj.keySet().iterator();
		
		String tempHTML = "";
		
		String temp = "";
		
		for(int i = 0; i < jsonObj.size(); i++)
		{
			temp = keys.next();
			
			if(temp.compareTo("charset") == 0)
			{
				tempHTML += "\n" + tab + "<meta " + temp + "=\"" + jsonObj.get(temp) + "\">";
			}
			else
			{
				tempHTML += "\n" + tab + "<meta " + "name=\"" + temp + "\" " + "content=\"" + jsonObj.get(temp) + "\">";
			}
		}
		
		return tempHTML;
	}
	public static String parseJSONObjectToHTMLString(JSONObject jsonObj)
	{
		JSONArray tempArray = new JSONArray();
		tempArray.add(jsonObj);
		
		String htmlString = "";
		
		//on regular JSON stored HTML document, jsonObj.size() should be 1, loop is here for unexpected formats
		for(int i = 0; i < tempArray.size(); i++)
		{
			
			//the i in the name indicates loop iteration
			
			//string for entire loop iteration
			String iHTMLString = "";
			
			String iDoctype = "";
			
			//string for <html> tag (and its attributes)
			String iHTML = "";
			
			String iHead = "";
			
			String iBody = "";
			
			String iCustomTags = "";
			
			
			//get all keys from current level of object as strings
			Iterator<String> keys = jsonObj.keySet().iterator();
			
			//in this level of JSON expected elements are: !doctype, <head>, <body>, and attributes of the <html> tag
			for(int j = 0; j < jsonObj.size(); j++)
			{
				String temp = keys.next();
				//System.out.println(temp);
				
				if(temp.compareTo("doctype") == 0)
				{
					iDoctype = "<!DOCTYPE " + ((JSONObject)(tempArray.get(i))).get(temp) + ">";
				}
				else if(temp.compareTo("head") == 0)
				{
					iHead += parseJSONObjectRecursiveHead((JSONObject)jsonObj.get(temp), 2);
				}
				else if(temp.compareTo("body") == 0)
				{
					iBody = parseJSONObjectRecursiveTag((JSONObject)jsonObj.get(temp), 2) + iBody;
				}
				else if(temp.compareTo("attributes") == 0)
				{
					//send as attribute, because it can return 1 string that is inserted into the tag
					iHTML += parseJSONObjectRecursiveAttribute((JSONObject)jsonObj.get(temp));
				}
				else if(temp.compareTo("language") == 0)
				//due to example files, unique rule for "language", as it has to be "lang" in output html. If input is lang already, it is normally handled as an attribute in the next else if
				{
					iHTML += (" lang" + "=\"" + ((JSONObject)(tempArray.get(i))).get(temp) + "\"");
				}
				//check if key is an HTML Global Attribute
				else if(temp.compareTo("accesskey") == 0 || temp.compareTo("class") == 0 || temp.compareTo("contenteditable") == 0 || temp.compareTo("dir") == 0 || temp.compareTo("draggable") == 0 || temp.compareTo("hidden") == 0 || temp.compareTo("id") == 0 || temp.compareTo("lang") == 0 || temp.compareTo("spellcheck") == 0 || temp.compareTo("style") == 0 || temp.compareTo("tabindex") == 0 || temp.compareTo("title") == 0 || temp.compareTo("translate") == 0)
				{
					iHTML += parseJSONObjectRecursiveAttribute((JSONObject)jsonObj.get(temp));
					
				}
				//check for global attribute data (handled separately because data is not a fixed tag)
				else if(temp.length() >= 5 && temp.substring(0, 4).compareTo("data-") == 0)
				{
					//TODO: fix for data attribute
					iHTML += parseJSONObjectRecursiveAttribute((JSONObject)jsonObj.get(temp));
				}	
				//in this level of iteration, assume remaining objects are custom tags. NOT IMPLEMENTED!
				else
				{
					iCustomTags += parseJSONObjectRecursiveTag((JSONObject)jsonObj.get(temp), 2);
				}
			}
			
			//add all Strings to output string of the loop iteration
			
			iHTMLString += iDoctype;
			
			iHTMLString += "\n" + "<html" + iHTML + ">";
			
				iHTMLString += "\n" + "\t" + "<head>" + iHead + "\n\t</head>";
				
				iHTMLString += "\n" + "\t" + "<body" + iBody + "\n\t</body>";
				
				//iHTMLString += "\n" + "\t" + "<" + tagName + "\"" + iCustomTags + "\n\t</" + tagName + ">";
			
			iHTMLString += "\n" + "</html>";
			
			htmlString += iHTMLString;
		}
		
		return htmlString;
	}

	public static void main(String[] args) {
		
		//import
		
		//set file name to know which file to target
		String fileName = "pageNotFound";
		
		JSONParser parser = new JSONParser();
		
		try (FileReader reader = new FileReader(fileName + ".json"))
		{
			Object obj = parser.parse(reader);
			
			//parse json to html string
			String htmlString = parseJSONObjectToHTMLString((JSONObject)obj);
			
			//print string for debug
			//System.out.print(htmlString);
			
			try (FileOutputStream fos = new FileOutputStream(fileName + ".html"))
			{
	            byte[] mybytes = htmlString.getBytes();
	            
	            fos.write(mybytes);
	        }
			
		}
		catch (FileNotFoundException e)
		{
			System.out.print("FileNotFoundException");
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			System.out.print("ParseException");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.print("IOException");
			e.printStackTrace();
		}
	}
}
