package IntermediairesREST;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * REST resource that provides a list of projects in JSON format.
 */
@Path("/getInProgList")
@Named
public class GetInProgList {

    @ComponentImport
    private PermissionManager permissionManager;
    @ComponentImport
    private JiraAuthenticationContext authenticationContext;

    /**
     * Constructor.
     * @param authenticationContext context that contains authenticated user
     * @param permissionManager the JIRA object which manages permissions for users and projects
     */
    @Inject
    public GetInProgList(JiraAuthenticationContext authenticationContext,
                         PermissionManager permissionManager) {
        this.authenticationContext = authenticationContext;
        this.permissionManager = permissionManager;
    }

    /**
     * Returns the list of projects browsable by the user in the specified request.
     *
     * @param request the context-injected {@code HttpServletRequest}
     * @return a {@code Response} with the marshalled projects
     */
    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getList(@Context HttpServletRequest request,
                            @QueryParam("CTProjID") String CTProjID) {




        JSONArray childJsonArray = new JSONArray();
        JSONObject parentJsonObject = new JSONObject();

        try {


            int IDproject = Integer.parseInt(CTProjID.replaceAll("[^0-9]", ""));
            String IDproj = String.valueOf(IDproject);

            //cal url api et recoit la reponse en String
            URL url = new URL ("http://localhost:8080/rest/api/2/project");
            //  String encoding = Base64.getEncoder().encodeToString(("test1:test1").getBytes(‌"UTF‌​-8"​));
            String encoding = new String(org.apache.commons.codec.binary.Base64.encodeBase64(("ramzi:admin").getBytes()));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   =
                    new BufferedReader (new InputStreamReader(content));
            String line;
            String result = "";
            while ((line = in.readLine()) != null)
            {

                result=result + line;
            }

            JSONParser parse = new JSONParser();
            //Type caste the parsed json data in json object
            JSONArray jArray = (JSONArray) parse.parse(result);



            int size = jArray.size();


            for( int i=0; i< size; i ++  )

            {
                JSONObject Jobj = (JSONObject) jArray.get(i);
                Object id = Jobj.get("id");
                String idS = id.toString();
                if ( idS.equals(IDproj))
                {

                    Object key = Jobj.get("key");
                    String keyS=key.toString();

                    //********** 2eme call


                    //cal url api et recoit la reponse en String
                    URL url2 = new URL ("http://localhost:8080/rest/projectconfig/1/workflowscheme/"+keyS+"");
                    //  String encoding = Base64.getEncoder().encodeToString(("test1:test1").getBytes(‌"UTF‌​-8"​));
                    String encoding2 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(("ramzi:admin").getBytes()));

                    HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                    connection2.setRequestMethod("GET");
                    connection2.setDoOutput(true);
                    connection2.setRequestProperty  ("Authorization", "Basic " + encoding2);
                    InputStream content2 = (InputStream)connection2.getInputStream();
                    BufferedReader in2   =
                            new BufferedReader (new InputStreamReader(content2));
                    String line2;
                    String result2 = "";
                    while ((line2 = in2.readLine()) != null)
                    {

                        result2=result2 + line2;
                    }

                    JSONParser parse2 = new JSONParser();
                    //Type caste the parsed json data in json object
                    JSONObject Jobj2 = (JSONObject) parse2.parse(result2);

                    JSONArray mappings = (JSONArray) Jobj2.get("mappings");

                    JSONObject firstMap = (JSONObject) mappings.get(0);
                    Object WorkflowName = firstMap.get("name");
                    String WorkflowNameS=WorkflowName.toString();



                    //*********3eme call


                    //cal url api et recoit la reponse en String
                    // http://localhost:8080/rest/projectconfig/1/workflow?workflowName=OkayWF&projectKey=DP
                    URL url3 = new URL ("http://localhost:8080/rest/projectconfig/1/workflow?workflowName="+WorkflowNameS+"&projectKey="+keyS);
                    //  String encoding = Base64.getEncoder().encodeToString(("test1:test1").getBytes(‌"UTF‌​-8"​));
                    String encoding3 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(("ramzi:admin").getBytes()));

                    HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
                    connection3.setRequestMethod("GET");
                    connection3.setDoOutput(true);
                    connection3.setRequestProperty  ("Authorization", "Basic " + encoding3);
                    InputStream content3 = (InputStream)connection3.getInputStream();
                    BufferedReader in3   =
                            new BufferedReader (new InputStreamReader(content3));
                    String line3;
                    String result3 = "";
                    while ((line3 = in3.readLine()) != null)
                    {

                        result3=result3 + line3;
                    }

                    JSONParser parse3 = new JSONParser();
                    //Type caste the parsed json data in json object
                    JSONObject Jobj3 = (JSONObject) parse2.parse(result3);
                    JSONArray sources = (JSONArray) Jobj3.get("sources");


                    int size3 = sources.size();
                    String[] tabS = new String[size3];

                    int it=0;


                    for( int j=0; j< size3; j ++  )

                    {
                        JSONObject Jobject_j = (JSONObject) sources.get(j);
                        JSONObject fromStatus = (JSONObject) Jobject_j.get("fromStatus");
                        JSONObject statusCategory = (JSONObject) fromStatus.get("statusCategory");
                        Object StatusName = fromStatus.get("name");
                        String StatusNameS= StatusName.toString();
                        Object statusCategoryName = statusCategory.get("name");
                        String  CategoryName=statusCategoryName.toString();
                        if ( CategoryName.equals("In Progress")) {

                            tabS[it]=StatusNameS;
                            it++;

                        }




                    }


                    //*********fin calls

                    for (int k =0; k<it; k++)
                    {

                        JSONObject ch = new JSONObject();
                        ch.put("label",tabS[k]);
                        ch.put("value",tabS[k]);
                        childJsonArray.add(ch);



                    }
                   // parentJsonObject.put("choix", childJsonArray);

                }

            }


        }

        catch (Exception e) {
            e.printStackTrace();
        }






        return Response.ok(childJsonArray).build();
    }
}
