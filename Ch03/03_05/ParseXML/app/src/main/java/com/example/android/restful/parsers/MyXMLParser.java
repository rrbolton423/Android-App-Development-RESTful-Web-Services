package com.example.android.restful.parsers;

import com.example.android.restful.model.DataItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romellbolton on 2/10/18.
 */

// This class has a single public static method named parseFeed().
// It returns an array of DataItem objects
public class MyXMLParser {

    public static DataItem[] parseFeed(String content) {

        try {

            // Boolean to keep tract when we are in a XML "item" tag
            boolean inItemTag = false;

            // String of the currentTagName
            String currentTagName = "";

            // The current DataItem
            DataItem currentItem = null;

            // ArrayList of DataItems
            List<DataItem> itemList = new ArrayList<>();

            // Create an instance of the XMLPullParserFactory class
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            // Create an instance of the XMLPullParser class
            XmlPullParser parser = factory.newPullParser();

            // Take the content that was passed into the method, the web service feed,
            // and passing it to the Parser object with it's setInput() method.
            parser.setInput(new StringReader(content));

            // Create an integer named eventType, and it's going to contain the current event
            // from the parser.
            int eventType = parser.getEventType();

            // While loop causes the parser to go through the XML content until it hits an event
            // called "END_DOCUMENT".
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    // I'm handling 3 events, named START_TAG, END_TAG, and TEXT. The START_TAG event
                    // tells me that I'm beginning an element. And so I'm using a method named "getName()",
                    // to find out what the element's name is. And then, if I'm starting an item element,
                    // I create a new instance of the DataItem class, and add it to my list.
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("item")) {
                            inItemTag = true;
                            currentItem = new DataItem();
                            itemList.add(currentItem);
                        }
                        break;

                    // If I hit an END_TAG of the element, I change this flag inItemTag boolean to false.
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            inItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    // And finally for the TEXT event, I'm grabbing the text and storing it, and then looking
                    // for the various element names that represent fields of a DataItem. And wherever I find that
                    // information, I save it into the current DataItem
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (inItemTag && currentItem != null) {
                            try {
                                switch (currentTagName) {
                                    case "itemName":
                                        currentItem.setItemName(text);
                                        break;
                                    case "description":
                                        currentItem.setDescription(text);
                                        break;
                                    case "category":
                                        currentItem.setCategory(text);
                                        break;
                                    case "price":
                                        currentItem.setPrice(Double.parseDouble(text));
                                        break;
                                    case "sort":
                                        currentItem.setSort(Integer.parseInt(text));
                                        break;
                                    case "image":
                                        currentItem.setImage(text);
                                    default:
                                        break;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }

                // At the end of the while loop, I'm calling a method named parser.next(),
                // and that's what moves the parser to the next event. I'm handling 3 events,
                // named START_TAG, END_TAG, and TEXT.
                eventType = parser.next();

            } // end while loop

            // Finally at the end, I'm converting the list into an array. I need to do this because I
            // need to be able to pass this data from my service back up to my visual layer, and I
            // can't pass a list object in an Intent. It has to be an array of Parcelable objects.
            // So these 2 bits of code are turning the list into an array and returning the data.

            // Create an Array of data items and specify how much space it needs to hold
            // the items by giving the size of the ArrayList
            DataItem[] dataItems = new DataItem[itemList.size()];

            // toArray() fills the array (dataItems) with contents of the ArrayList (list)
            return itemList.toArray(dataItems);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
