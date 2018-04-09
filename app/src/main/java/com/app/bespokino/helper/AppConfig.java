package com.app.bespokino.helper;

import com.app.bespokino.model.FrontToolValues;
import com.app.bespokino.model.MeasuringToolTableValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 7/31/2017 AD.
 */

public class AppConfig {

    public static  int updatedCustomerId;
    public static  int updatedOrderNo;
    public static  int updatedPaperNo;



    public static int mCartItemCount;
    public static int userID;
    public static int modelNo;

    public static double customerNeckValue;
    public static double customerBiceptsValue;
    public static double customerlengthValue;
    public static double customerCuffValue;
    public static double customerSleeve;



    //back
    public static double customerShoulderValue;
    public static double customerChestValue;
    public static double customerWaistValue;
    public static double CustomerHipsValue;



    public static String NeckValue;
    public static String BiceptsValue;
    public static String lengthValue;
    public static String CuffValue;
    public static String Sleeve;



    //back
    public static String ShoulderValue;
    public static String ChestValue;
    public static String WaistValue;
    public static String HipsValue;

    //Additional option items
    public static String placket = "";
    public static String backPleats = "";
    public static String pocket = "";
    public static String shortSleeve = "";
    public static String tuxedo = "";
    public static String tuxedoPleat = "";
    public static String collarContrastFabric = "";
    public static String cuffContrastFabric = "";
    public static String placketContrastFabric = "";
    public static String sleeveVentContrastFabric = "";
    public static String whiteCuffAndCollar = "";
    public static String contrastFabricCategory = "";
    public static String contrastFabricID = "";
    public static String buttonholeColor = "";
    public static String btnType = "save";

    public static List<FrontToolValues>frontToolValues=new ArrayList<>();
    public static String fabricImageSelected ;
    static List<String>backToolValues=new ArrayList<>();


  public static String  abdoman=null,chest = null,pelvis = null,shoulders = null,postures = null;




}
