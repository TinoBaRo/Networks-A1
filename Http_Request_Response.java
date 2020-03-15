package HTTP_ClientLibrary;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;
import java.nio.file.*;

public class Http_Request_Response {
    /*
     * This class only has static methods.
     */

    /* ***** 2. httpc get usage: httpc get [-v] [-h key:value] URL *****
     * httpc get http://httpbin.org/get?course=networking&assignment=1
     * httpc get -v http://httpbin.org/get?course=networking&assignment=1
     * httpc get -h "X-Header: Value" https://www.keycdn.com/
     * httpc get -v -h "X-Header: Value" https://www.keycdn.com/
     */
    public static void getRequest(String stringRequest) {
        String[] splitArr = stringRequest.split(" ");

        String urlPath = null;
        String version = "HTTP/1.0";
        Vector headers = new Vector();
        // String entityBody = null; // no entity body in get method (there is one for the post)

        // GET THE URL
        for (int i = 1; i < splitArr.length; i++)
        {
            if (splitArr[i].contains("http"))
            {
                urlPath = splitArr[i];
            }
        }

        // httpc get http://httpbin.org/get?course=networking&assignment=1
        if (!stringRequest.contains("-h") && !stringRequest.contains("-v"))
        {
            // ----- Establish connection -----
            try
            {
                URL url = new URL(urlPath);
                InetAddress address = InetAddress.getByName(url.getHost());

                String temp = address.toString();
                //System.out.println("(EXTRA) temp: " + temp);

                String[] splitAddress = temp.split("/");
                String ip = splitAddress[splitAddress.length - 1];
                //System.out.println("(EXTRA) IP: " + ip);

                if (ip != null) {
                    // SOCKET
                    Socket socket = new Socket("localhost", 8080);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    Scanner in = new Scanner(socket.getInputStream()); //.useDelimiter("\\n");

                    // Doing Get
                    out.write("GET " + url.getPath() + " HTTP/1.0\r\nUser-Agent: Concordia\r\n\r\n");
                    out.flush();

                    String responseString = "";
                    while(in.hasNextLine())
                    {
                        responseString += in.nextLine() + "\n"; // Even entity body will have "\n" at the end (not big deal, just for parsing)
                    }
                    in.close();
                    socket.close();

                    // SPLIT the response, to get the Entity Body of the response completely separate
                    System.out.print("RESPONSE: \n" + responseString + "\n");

                    /*
                    String[] splitResponse = responseString.split("\n\n");

                    // since there is no -v, only display the Entity Body of the response
                    if (splitResponse.length > 1) // ENSURE THERE IS ENTITY BODY
                    {
                        System.out.println(splitResponse[1]);
                    } else
                        System.out.println("No entity Body");

                     */

                }
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }
        }// END OF CASE

        // httpc get -h Content-Type:application/json -v http://httpbin.org/get?course=networking&assignment=1
        else if (stringRequest.contains("-h") || stringRequest.contains("-v"))
        {
            for (int i = 1; i < splitArr.length; i++)
            {
                if (splitArr[i].equals("-h"))
                {
                    headers.add(splitArr[i+1] + "\r\n"); // the element after -h is "Header:Value"
                }
            }

            String headerLine = "";
            if(headers.size() > 0)
            {
                for (int i = 0; i < headers.size(); i++) {
                    headerLine += headers.elementAt(i);
                }
            }

            // ----- Establish connection -----
            try
            {
                URL url = new URL(urlPath);
                InetAddress address = InetAddress.getByName(url.getHost());

                String temp = address.toString();
                //System.out.println("(EXTRA) temp: " + temp);

                String[] splitAddress = temp.split("/");
                String ip = splitAddress[splitAddress.length - 1];
                //System.out.println("(EXTRA) IP: " + ip);

                if (ip != null) {
                    // SOCKET
                    Socket socket = new Socket("localhost", 8080);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    Scanner in = new Scanner(socket.getInputStream()); //.useDelimiter("\\n");

                    // Doing Get
                    out.write("GET " + url.getPath() + " HTTP/1.0\r\n" + headerLine + "\r\n");
                    out.flush();


                    String responseString = "";
                    while(in.hasNextLine())
                    {
                        responseString += in.nextLine() + "\n";
                    }

                    in.close();
                    socket.close();

                    // SPLIT the response, to get the Entity Body of the response completely separate
                    System.out.print("RESPONSE: \n" + responseString + "\n");

                    /*
                    String[] splitResponse = responseString.split("\n\n");

                    if(stringRequest.contains("-v") == true )
                    {
                        System.out.println(responseString); // entire response
                    }
                    else if (splitResponse.length > 1) // ENSURE THERE IS ENTITY BODY
                    {
                        System.out.println(splitResponse[1]);
                    } else
                        System.out.println("No entity Body");

                     */
                }
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }
        } // END OF CASE
        else
            System.out.println("httpc is a curl-like application but supports HTTP protocol only.\n" +
                    "Usage:\n" +
                    "    httpc command [arguments]\n" +
                    "The commands are:\n" +
                    "    get     executes a HTTP GET request and prints the response.\n" +
                    "    post    executes a HTTP POST request and prints the response.\n" +
                    "    help    prints this screen.\n" +
                    "Use \"httpc help [command]\" for more information about a command.\n");

    }// END-METHOD-GET



    /* ***** 3. httpc post usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL *****
     * httpc post -v
     * httpc post -h
     * httpc post -d
     * httpc post -f
     * with query
     * Either [-d] or [-f] can be used but not both.
     */
    public static void postRequest(String stringRequest)
    {
        String[] splitArr = stringRequest.split(" ");

        String urlPath = null;
        String version = "HTTP/1.0";
        Vector headers = new Vector();
        String entityBody = ""; // we also care about the length of the entity-body
        String contentLengthHeader = "";

        // header-line
        String headerLine = "";

        // GET THE URL
        for (int i = 1; i < splitArr.length; i++)
        {
            if (splitArr[i].contains("http"))
            {
                urlPath = splitArr[i];
            }
        }

        // httpc post -d {Assignment: 1} -v -h Content-Type:application/json http://httpbin.org/filePath
        // ONLY -d
        if( stringRequest.contains("-d") && !stringRequest.contains("-f") )
        {
            if(stringRequest.contains("-h"))
            {
                for (int i = 1; i < splitArr.length; i++)
                {
                    if (splitArr[i].equals("-h"))
                    {
                        headers.add(splitArr[i + 1] + "\r\n"); // the element after -h is "Header:Value"
                    }
                }

                if (headers.size() > 0)
                {
                    for (int i = 0; i < headers.size(); i++)
                    {
                        headerLine += headers.elementAt(i);
                    }
                }
            }

            // "Entity-Body", it's the inline data
            entityBody = stringRequest.substring( stringRequest.indexOf("{") , stringRequest.indexOf("}")+1 );

            contentLengthHeader = "Content-Length:" + Integer.toString( entityBody.length() ) + "\r\n";
            // Add it to the header-line
            headerLine += contentLengthHeader;


            // ----- Establish connection -----
            try
            {
                URL url = new URL(urlPath);
                InetAddress address = InetAddress.getByName(url.getHost());
                String temp = address.toString();
                //System.out.println("(EXTRA) temp: " + temp);

                String[] splitAddress = temp.split("/");
                String ip = splitAddress[splitAddress.length - 1];
                //System.out.println("(EXTRA) IP: " + ip);

                if (ip != null)
                {
                    // SOCKET
                    Socket socket = new Socket("localhost", 8080);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    Scanner in = new Scanner(socket.getInputStream()); //.useDelimiter("\\n");

                    // NOTE, last headerLine has another "\r\n"
                    out.write("POST " + url.getPath() + " HTTP/1.0\r\n" + headerLine + "\r\n" + entityBody);
                    out.flush();


                    String responseString = "";
                    while(in.hasNextLine())
                    {
                        responseString += in.nextLine() + "\n";
                    }

                    in.close();
                    socket.close();

                    // HANDLE VERBOSITY
                    // -v
                    System.out.print("RESPONSE: \n" + responseString + "\n");

                    /*
                    String[] splitResponse = responseString.split("\n\n");

                    if(stringRequest.contains("-v") == true )
                    {
                        System.out.println(responseString); // entire response
                    }
                    else if (splitResponse.length > 1) // ENSURE THERE IS ENTITY BODY
                    {
                        System.out.println(splitResponse[1]);
                    } else
                        System.out.println("No entity Body");

                     */

                }
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // ONLY -f
        // httpc post -f "loremipsum.txt" -v http://httpbin.org/post
        else if( stringRequest.contains("-f") && !stringRequest.contains("-d") )
        {
            if(stringRequest.contains("-h"))
            {
                for (int i = 1; i < splitArr.length; i++)
                {
                    if (splitArr[i].equals("-h"))
                    {
                        headers.add(splitArr[i + 1] + "\r\n"); // the element after -h is "Header:Value"
                    }
                }

                if (headers.size() > 0)
                {
                    for (int i = 0; i < headers.size(); i++)
                    {
                        headerLine += headers.elementAt(i);
                    }
                }
            }

            /* "Entity-Body", it's the String in the given file
            * The file is in between "loremipsum.txt"
            */
            String fileName = stringRequest.substring( stringRequest.indexOf("\"")+1 , stringRequest.lastIndexOf("\""));
            try
            {
                String data = "";
                data = new String(Files.readAllBytes(Paths.get("/Users/Celestino/Desktop/"+fileName)));
                //System.out.println(data);

                // Entity-body is the data inside the given file (file has desktop path)
                entityBody = data;
            }
            catch (IOException e)
            {
                System.out.println(e);
            }

            contentLengthHeader = "Content-Length:" + Integer.toString( entityBody.length() ) + "\r\n";
            // Add it to the header-line
            headerLine += contentLengthHeader;


            // ----- Establish connection -----
            try
            {
                URL url = new URL(urlPath);
                InetAddress address = InetAddress.getByName(url.getHost());
                String temp = address.toString();
                //System.out.println("(EXTRA) temp: " + temp);

                String[] splitAddress = temp.split("/");
                String ip = splitAddress[splitAddress.length - 1];
                //System.out.println("(EXTRA) IP: " + ip);

                if (ip != null)
                {
                    // SOCKET
                    Socket socket = new Socket("localhost", 8080);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    Scanner in = new Scanner(socket.getInputStream()); //.useDelimiter("\\n");

                    // NOTE, last headerLine has another "\r\n"
                    out.write("POST " + url.getPath() + " HTTP/1.0\r\n" + headerLine + "\r\n" + entityBody);
                    out.flush();


                    String responseString = "";
                    while(in.hasNextLine())
                    {
                        responseString += in.nextLine() + "\n";
                    }

                    in.close();
                    socket.close();

                    //System.out.println(responseString);

                    // HANDLE VERBOSITY
                    // -v
                    System.out.print("RESPONSE: \n" + responseString + "\n");

                    /*
                    String[] splitResponse = responseString.split("\n\n");

                    if(stringRequest.contains("-v") == true )
                    {
                        System.out.println(responseString); // entire response
                    }
                    else if (splitResponse.length > 1) // ENSURE THERE IS ENTITY BODY
                    {
                        System.out.println(splitResponse[1]);
                    } else
                        System.out.println("No entity Body");

                     */

                }
            } catch (UnknownHostException u) {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // not correct usage
        else
            System.out.println("httpc is a curl-like application but supports HTTP protocol only.\n" +
                    "Usage:\n" +
                    "    httpc command [arguments]\n" +
                    "The commands are:\n" +
                    "    get     executes a HTTP GET request and prints the response.\n" +
                    "    post    executes a HTTP POST request and prints the response.\n" +
                    "    help    prints this screen.\n" +
                    "Use \"httpc help [command]\" for more information about a command.\n");


    }// END-METHOD-POST

}// END LIBRARY
