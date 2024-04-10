package com.j0k3r;

import com.j0k3r.models.Exchanges;
import com.j0k3r.services.ChangeService;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String MONEDAS = """
            USD	AED	AFN	ALL	AMD	ANG	AOA	ARS	AUD	AWG	AZN	BAM	BBD	BDT	BGN	BHD	BIF
            BMD	BND	BOB	BRL	BSD	BTN	BWP	BYN	BZD	CAD	CDF	CHF	CLP	CNY	COP	CRC	CUP
            CVE	CZK	DJF	DKK	DOP	DZD	EGP	ERN	ETB	EUR	FJD	FKP	FOK	GBP	GEL	GGP	GHS
            GIP	GMD	GNF	GTQ	GYD	HKD	HNL	HRK	HTG	HUF	IDR	ILS	IMP	INR	IQD	IRR	ISK
            JEP	JMD	JOD	JPY	KES	KGS	KHR	KID	KMF	KRW	KWD	KYD	KZT	LAK	LBP	LKR	LRD
            LSL	LYD	MAD	MDL	MGA	MKD	MMK	MNT	MOP	MRU	MUR	MVR	MWK	MXN	MYR	MZN	NAD
            NGN	NIO	NOK	NPR	NZD	OMR	PAB	PEN	PGK	PHP	PKR	PLN	PYG	QAR	RON	RSD	RUB
            RWF	SAR	SBD	SCR	SDG	SEK	SGD	SHP	SLE	SLL	SOS	SRD	SSP	STN	SYP	SZL	THB
            TJS	TMT	TND	TOP	TRY	TTD	TVD	TWD	TZS	UAH	UGX	UYU	UZS	VES	VND	VUV	WST
            XAF	XCD	XDR	XOF	XPF	YER	ZAR	ZMW	ZWL""";

    public static void main(String[] args) {
        ChangeService changeService = new ChangeService();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    *******************************************************************
                        Bienvenido al convertidor de monedas
                        Por favor ingrese la moneda que quiere convertir:
                        ("monedas" para ver las monedas disponibles)
                        ("exit" para salir del programa)
                    *******************************************************************""");
            String currency = scanner.nextLine().toUpperCase();
            if (currency.equals("EXIT")) {
                break;
            }
            if (currency.equals("MONEDAS")) {
                cleanScreen();
                System.out.printf("""
                        *******************************************************************
                            Moneda seleccionada: %s
                            Las monedas disponibles son:
                        *******************************************************************%n""", currency);
                System.out.println(MONEDAS);
                System.out.println("""
                *******************************************************************""");
                continue;
            }
            if (!MONEDAS.contains(currency)){
                System.out.println("""
                        *******************************************************************
                            La moneda ingresada no es válida
                        *******************************************************************""");
                continue;
            }

            Exchanges exchanges = null;
            try {
                exchanges = changeService.getExchangesRates(currency);
            } catch (IOException | InterruptedException e) {
                System.out.println("""
                        *******************************************************************
                            Error al obtener las tasas de cambio
                            Intente de nuevo o pruebe mas tarde, disculpe las molestias
                            Asegurese se verificar su conexion a internet
                        *******************************************************************""");
                continue;
            }
            Map<String, Double> rates = exchanges.getConversion_rates();
            cleanScreen();
            System.out.println("""
                    *******************************************************************
                        Las monedas disponibles son:
                    *******************************************************************""");
            int counter = 0;
            for (String key : rates.keySet()) {
                System.out.print(key + "\t");
                counter++;
                if (counter == 17) {
                    System.out.println();
                    counter = 0;
                }
            }
            System.out.println();
            System.out.println("""
                    *******************************************************************
                        Por favor ingrese la moneda a la que desea convertir:
                    *******************************************************************""");
            String currencyTo = scanner.nextLine().toUpperCase();
            if (rates.containsKey(currencyTo)) {
                System.out.println("""
                        *******************************************************************
                            Por favor ingrese la cantidad a convertir:
                        *******************************************************************""");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                double rate = rates.get(currencyTo);
                System.out.printf("""
                        *******************************************************************
                            El resultado de la conversión es:
                            %.2f %s = %.2f %s
                        %n""", amount, currency, changeService.change(amount, rate), currencyTo);
            } else {
                System.out.println("""
                        *******************************************************************
                            La moneda ingresada no es válida
                        *******************************************************************""");
            }
        }
        System.out.println("""
                *******************************************************************
                    Gracias por usar el convertidor de monedas
                *******************************************************************""");
    }

    public static void cleanScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}