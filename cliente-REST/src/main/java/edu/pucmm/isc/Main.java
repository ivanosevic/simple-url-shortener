package edu.pucmm.isc;

import edu.pucmm.isc.urls.URL_POST;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.Scanner;

public class Main {

    public static void main(String args[]){

        Scanner inputUser = new Scanner(System.in);
        Scanner inputPassword = new Scanner(System.in);
        Scanner inputOption = new Scanner(System.in);
        Scanner inputUrl = new Scanner(System.in);
        Scanner inputOtherUser = new Scanner(System.in);

        String username;
        String password;
        String otherUser;
        int option = 0;
        User_GET loggedUser = null;
        boolean logged;

        while(true)
        {
            do {

                System.out.println();
                System.out.println("Digite sus credenciales para iniciar la sesion");
                System.out.println();
                System.out.println("Usuario: ");
                username = inputUser.nextLine();
                System.out.println("Contraseña: ");
                password = inputPassword.nextLine();

                User_POST user = new User_POST(username, password);

                logged = Unirest.post("http://localhost:7001/login")
                        .header("Content-Type", "application/json")
                        .body(user).asEmpty().isSuccess();

                if (logged) {

                    var unirestResponse = Unirest.post("http://localhost:7001/login")
                            .header("Content-Type", "application/json")
                            .body(user).asJson().getBody();


                    loggedUser = new User_GET(
                            unirestResponse.getObject().get("token").toString(),
                            unirestResponse.getObject().get("name").toString(),
                            unirestResponse.getObject().get("lastname").toString(),
                            unirestResponse.getObject().get("username").toString(),
                            unirestResponse.getObject().get("email").toString(),
                            unirestResponse.getObject().getJSONArray("roles").toList()
                    );

                    System.out.println();
                    System.out.println("Bienvenido " + loggedUser.getName() + " " + loggedUser.getLastname());
                }

            }while(!logged);

            while(logged)
            {

                System.out.println();
                System.out.println("Elija una opcion:");
                System.out.println("1 - Acortar URL");
                System.out.println("2 - Ver URLs de un usuario");
                System.out.println("3 - Cerrar Sesion");
                option = inputOption.nextInt();

                switch(option)
                {

                    case 1:

                        System.out.println();
                        System.out.println("Digite la URL a acortar: ");
                        URL_POST url = new URL_POST(inputUrl.nextLine());
                        System.out.println();
                        System.out.println("URL acortada con exito!. Mostrando su informacion: ");
                        JSONObject info = Unirest.post("http://localhost:7001/short-url")
                                .header("Authorization", loggedUser.getToken())
                                .header("Content-Type","application/json")
                                .body(url).asJson().getBody().getObject();

                        System.out.println("Usuario: " + info.get("user"));
                        System.out.println("URL original: " + info.get("longUrl"));
                        System.out.println("URL Acortada: " + info.get("shortUrl"));
                        System.out.println("Fecha de creacion: " + info.get("createdAt"));
                        System.out.println("Estadisticas: ");

                        JSONObject stats = info.getJSONObject("statistics");
                        System.out.println("Clicks: " + stats.get("clicks"));
                        System.out.println("Clicks unicos: " + stats.get("uniqueClicks"));
                        System.out.println("Click en las ultimas 24 horas: " + stats.get("clicksLast24Hours"));

                        System.out.println("Agrupado por Sistema Operativo: ");
                        JSONObject OS = stats.getJSONObject("groupedByOs");
                        System.out.println("Series: " + OS.getJSONArray("series"));
                        System.out.println("Labels: " + OS.getJSONArray("labels"));

                        System.out.println("Agrupado por Navegador: ");
                        JSONObject Browser = stats.getJSONObject("groupedByBrowser");
                        System.out.println("Series: " + Browser.getJSONArray("series"));
                        System.out.println("Labels: " + Browser.getJSONArray("labels"));

                        System.out.println("Agrupado por Plataforma: ");
                        JSONObject Platform = stats.getJSONObject("groupedByPlatform");
                        System.out.println("Series: " + Platform.getJSONArray("series"));
                        System.out.println("Labels: " + Platform.getJSONArray("labels"));

                        System.out.println("Clicks por Pais: ");
                        JSONObject Country = stats.getJSONObject("clicksByCountry");
                        System.out.println("Series: " + Country.getJSONArray("series"));
                        System.out.println("Labels: " + Country.getJSONArray("labels"));

                        break;

                    case 2:

                        System.out.println();
                        System.out.println("Digite el nombre del usuario: ");
                        otherUser = inputOtherUser.nextLine();
                        System.out.println();

                       JSONArray unirestResponse = Unirest.get("http://localhost:7001/users/"+otherUser+"/urls?page=1")
                                .header("Authorization", loggedUser.getToken())
                                .header("Content-Type","application/json")
                                .asJson().getBody().getObject().getJSONArray("results");

                        for(int i = 0; i < unirestResponse.length(); i++)
                        {
                            System.out.println("URL #"+(i+1));
                            JSONObject jsonObject = (JSONObject) unirestResponse.get(i);
                            System.out.println("Usuario: " + jsonObject.get("user"));
                            System.out.println("URL original: " + jsonObject.get("longUrl"));
                            System.out.println("URL Acortada: " + jsonObject.get("shortUrl"));
                            System.out.println("Fecha de creacion: " + jsonObject.get("createdAt"));
                            System.out.println("Estadisticas: ");

                            JSONObject statistic = jsonObject.getJSONObject("statistics");
                            System.out.println("Clicks: " + statistic.get("clicks"));
                            System.out.println("Clicks unicos: " + statistic.get("uniqueClicks"));
                            System.out.println("Click en las ultimas 24 horas: " + statistic.get("clicksLast24Hours"));

                            System.out.println("Agrupado por Sistema Operativo: ");
                            JSONObject OS_ = statistic.getJSONObject("groupedByOs");
                            System.out.println("Series: " + OS_.getJSONArray("series"));
                            System.out.println("Labels: " + OS_.getJSONArray("labels"));

                            System.out.println("Agrupado por Navegador: ");
                            JSONObject Browser_ = statistic.getJSONObject("groupedByBrowser");
                            System.out.println("Series: " + Browser_.getJSONArray("series"));
                            System.out.println("Labels: " + Browser_.getJSONArray("labels"));

                            System.out.println("Agrupado por Plataforma: ");
                            JSONObject Platform_ = statistic.getJSONObject("groupedByPlatform");
                            System.out.println("Series: " + Platform_.getJSONArray("series"));
                            System.out.println("Labels: " + Platform_.getJSONArray("labels"));

                            System.out.println("Clicks por Pais: ");
                            JSONObject Country_ = statistic.getJSONObject("clicksByCountry");
                            System.out.println("Series: " + Country_.getJSONArray("series"));
                            System.out.println("Labels: " + Country_.getJSONArray("labels"));


                            System.out.println();
                        }

                        break;
                  
                    case 3:
                        loggedUser = null;
                        logged = false;
                        break;
                }
            }
        }


    }

}
