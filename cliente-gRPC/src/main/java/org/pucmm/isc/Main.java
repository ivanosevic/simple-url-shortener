package org.pucmm.isc;

import edu.pucmm.eict.urlshortener.grpc.generated.ShortUrlProto;
import edu.pucmm.eict.urlshortener.grpc.generated.ShortUrlServiceRnGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;


public class Main {

    public static void main(String args[])
    {
        String host = "localhost";
        int port = 7002;

        Scanner inputOption = new Scanner(System.in);
        Scanner inputUrl = new Scanner(System.in);
        Scanner inputOtherUser = new Scanner(System.in);
        int option;
        String url;
        String otherUser;

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost",port)
                .usePlaintext()
                .build();

        //opening the service
        ShortUrlServiceRnGrpc.ShortUrlServiceRnBlockingStub urlInterface = ShortUrlServiceRnGrpc.newBlockingStub(channel);


        while(true)
        {
            System.out.println();
            System.out.println("Elija una opcion:");
            System.out.println("1 - Acortar URL");
            option = inputOption.nextInt();

            switch(option){

                case 1:
                    System.out.println();
                    System.out.println("Digite la URL a acortar: ");
                    url = inputUrl.nextLine();

                    //consultando el servicio
                   ShortUrlProto.ShortUrlResponse urlResponse = urlInterface.shortUrl(
                            ShortUrlProto.ShortUrlRequest.newBuilder().setUrl(url).build()
                    );

                    System.out.println();
                    System.out.println("URL acortada con exito! Mostrando su informacion: ");
                    System.out.println("Usuario: " + urlResponse.getUser());
                    System.out.println("URL original: " + urlResponse.getLongUrl());
                    System.out.println("URL Acortada: " + urlResponse.getShortUrl());
                    System.out.println("Fecha de creacion: " + urlResponse.getCreatedAt());
                    System.out.println("Estadisticas: ");


                    System.out.println("Clicks: " + urlResponse.getStatistics().getClicks());
                    System.out.println("Clicks unicos: " + urlResponse.getStatistics().getUniqueClicks());
                    System.out.println("Click en las ultimas 24 horas: " + urlResponse.getStatistics().getClicksLast24Hours());

                    System.out.println("Agrupado por Sistema Operativo: ");
                    System.out.println("Series: " + urlResponse.getStatistics().getGroupedByOs().getSeriesList());
                    System.out.println("Labels: " + urlResponse.getStatistics().getGroupedByOs().getLabelsList());

                    System.out.println("Agrupado por Navegador: ");
                    System.out.println("Series: " + urlResponse.getStatistics().getGroupedByBrowser().getSeriesList());
                    System.out.println("Labels: " + urlResponse.getStatistics().getGroupedByBrowser().getLabelsList());

                    System.out.println("Agrupado por Plataforma: ");
                    System.out.println("Series: " + urlResponse.getStatistics().getGroupedByPlatform().getSeriesList());
                    System.out.println("Labels: " + urlResponse.getStatistics().getGroupedByPlatform().getLabelsList());


                    System.out.println("Clicks por Pais: ");
                    System.out.println("Series: " + urlResponse.getStatistics().getClicksByCountry().getSeriesList());
                    System.out.println("Labels: " + urlResponse.getStatistics().getClicksByCountry().getLabelsList());

                    break;

            }

        }
    }
}
