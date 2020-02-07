package HTTP_ClientLibrary;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;

public class Http_Request_Response
{
    /*
    * This class only has static methods.
    */

    /* ***** 2. httpc get usage: httpc get [-v] [-h key:value] URL *****
    * httpc get 'http://httpbin.org/get?course=networking&assignment=1'
    * httpc get -v 'http://httpbin.org/get?course=networking&assignment=1'
    * httpc get -h "X-Header: Value" 'https://www.keycdn.com/'
    */
    public static void getRequest(String stringRequest)
    {
        String[] splitArr = stringRequest.split(" ");
/*
        for (int i=0; i<splitArr.length; i++)
        {
            System.out.println(splitArr[i]);
        }
*/
        String method = "get";
        String urlPath = null;
        String version = "HTTP/1.0";
        Vector headers = new Vector();
        // String entityBody = null; // no entity body in get method

        // httpc get 'http://httpbin.org/get?course=networking&assignment=1'
        if( !stringRequest.contains("-h") && !stringRequest.contains("-v") )
        {
            for (int i=1; i<splitArr.length; i++)
            {
                if(splitArr[i].contains("http"))
                {
                    urlPath = splitArr[i];
                }
            }
            // NOW - Establish connection
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
                    Socket socket = new Socket(ip, 80);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    Scanner in = new Scanner(socket.getInputStream());

                    // QUERY WOULD BE IN THE PATH
                    //System.out.println("(EXTRA) Path: " + url.getPath());

                    // ********** MOST IMPORTANT PART OF THE SET-UP **********
                    // 1. POST
                    // 2. GET
                    // In the case of the post, there could be an Entity Body
                    out.write("GET " + url.getPath() + " HTTP/1.0\r\nUser-Agent: Hello\r\n\r\n");
                    out.flush();

                    while (in.hasNextLine())
                    {
                        System.out.println(in.nextLine());
                    }

                    in.close();
                    socket.close();
                }
            }
            catch (UnknownHostException u)
            {
                System.out.println(u);
            } catch (IOException i) {
                System.out.println(i);
            }

        }

        // httpc get -v 'http://httpbin.org/get?course=networking&assignment=1'
        if( stringRequest.contains("-v") && !stringRequest.contains("-h") )
        {
            for (int i=1; i<splitArr.length; i++)
            {
                if(splitArr[i].contains("http"))
                {
                    urlPath = splitArr[i];
                }
            }
        }

        // httpc get -h "X-Header: Value" 'https://www.keycdn.com/'
        if( stringRequest.contains("-h") && !stringRequest.contains("-v") )
        {
            for (int i=1; i<splitArr.length; i++)
            {
                if(splitArr[i].equals("-h"))
                {
                    headers.add( splitArr[i]+1 ); // the element after -h is "Header:Value"
                }

                if(splitArr[i].contains("http"))
                {
                    urlPath = splitArr[i];
                }
            }
        }

    }// END-METHOD


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

    }


    public static void main(String[] args)
    {
        // Establish a connection
        try
        {
            // InetAddress web = InetAddress.getByName("http://httpbin.org/get?course=networking&assignment=1");

            URL url = new URL("https://www.google.ca/?q=hello+world");
            InetAddress address = InetAddress.getByName(url.getHost());

            String temp = address.toString();
            System.out.println("(EXTRA) temp: " + temp);

            String[] splitAddress = temp.split("/");

            String ip = splitAddress[splitAddress.length - 1];
            System.out.println("(EXTRA) IP: " + ip);

            if (ip != null)
            {
                // SOCKET
                Socket socket = new Socket(ip, 80);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                Scanner in = new Scanner(socket.getInputStream());

                // COULD BE DIFFERENT WITH QUERY STRING
                // QUERY WOULD BE IN THE PATH
                System.out.println("(EXTRA) Path: " + url.getPath());

                // ********** MOST IMPORTANT PART OF THE SET-UP **********
                // 1. POST
                // 2. GET
                // In the case of the post, there could be an Entity Body
                out.write("GET " + url.getPath() + " HTTP/1.0\r\nUser-Agent: Hello\r\n\r\n");
                out.flush();

                while (in.hasNextLine())
                {
                    System.out.println(in.nextLine());
                }

                in.close();
                socket.close();
/*
                // URL Input Stream Reader
                BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = input.readLine()) != null)
                {
                    System.out.println(inputLine);
                }
                input.close();
*/
            }
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

    }

}
