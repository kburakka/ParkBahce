package com.example.msi.demoo.utils;

import com.android.volley.toolbox.StringRequest;

public class Const {
//	public static String host_with_port = "http://78.46.197.92" + ":6080";
//
//	public static String feature_add__url = "http://192.168.20.128:6170" + "/geoserver";
//
//	public static String feature_attachment_get_url = "http://192.168.20.128:6170" + "/attachments/";

	public static String host_with_port = "http://78.46.197.92:6080";

	public static String feature_add__url = "http://78.46.197.92:6190/geoPark";

	public static String feature_attachment_get_url = "http://78.46.197.92:6190/attachmentsPark/";

	public static String percon_string = "{\n" +
			"  \"success\": true,\n" +
			"  \"username\": \"superadmin\",\n" +
			"  \"userID\": 1,\n" +
			"  \"moderation\": {\n" +
			"    \"isModerator\": null,\n" +
			"    \"moderationRequired\": null\n" +
			"  },\n" +
			"  \"layers\": [\n" +
			"\t{\n" +
			"      \"fields\": [\n" +
			"        {\n" +
			"          \"id\": 378,\n" +
			"          \"layer\": \"nokta\",\n" +
			"          \"field\": \"tip\",\n" +
			"          \"alias\": \"Tip\",\n" +
			"          \"domain\": true,\n" +
			"          \"domain_table\": \"nokta_tip\",\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"varchar\",\n" +
			"          \"position\": 1,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": [\n" +
			"            {\n" +
			"              \"id\": 1,\n" +
			"              \"desc\": \"IZGARA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 2,\n" +
			"              \"desc\": \"ELEKTRİK DİREĞİ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 3,\n" +
			"              \"desc\": \"HAVALANDIRMA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 4,\n" +
			"              \"desc\": \"REKLAM TABELASI\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 5,\n" +
			"              \"desc\": \"ROGAR KAPAĞI\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 6,\n" +
			"              \"desc\": \"PANO\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 7,\n" +
			"              \"desc\": \"BANK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 8,\n" +
			"              \"desc\": \"OYUN GRUBU\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 9,\n" +
			"              \"desc\": \"ÇÖP KUTUSU\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 10,\n" +
			"              \"desc\": \"KAPAK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 11,\n" +
			"              \"desc\": \"PİKNİK MASASI\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 12,\n" +
			"              \"desc\": \"VANA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 13,\n" +
			"              \"desc\": \"FİTNESS ALETİ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 14,\n" +
			"              \"desc\": \"ELEKTRIKLİ_PANO\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 15,\n" +
			"              \"desc\": \"SOKAK LAMBASI\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 16,\n" +
			"              \"desc\": \"DOĞALGAZ KUTUSU\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 17,\n" +
			"              \"desc\": \"TABELA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 18,\n" +
			"              \"desc\": \"BAYRAK DİREĞİ\"\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"nokta\",\n" +
			"          \"field\": \"ilce\",\n" +
			"          \"alias\": \"İlçe\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 2,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"nokta\",\n" +
			"          \"field\": \"mahalle\",\n" +
			"          \"alias\": \"Mahalle\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 3,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 68,\n" +
			"          \"layer\": \"nokta\",\n" +
			"          \"field\": \"touch_by\",\n" +
			"          \"alias\": \"Kullanıcı\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_by\",\n" +
			"          \"position\": 4,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 67,\n" +
			"          \"layer\": \"nokta\",\n" +
			"          \"field\": \"touch_date\",\n" +
			"          \"alias\": \"Tarih\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_date\",\n" +
			"          \"position\": 5,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        }\n" +
			"      ],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"nokta\",\n" +
			"      \"alias\": \"Nokta\",\n" +
			"      \"type\": \"MultiPoint\",\n" +
			"      \"workspace\": \"kgm\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },\n" +
			"\t{\n" +
			"      \"fields\": [\n" +
			"        {\n" +
			"          \"id\": 378,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"ad\",\n" +
			"          \"alias\": \"Ad\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"varchar\",\n" +
			"          \"position\": 1,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 377,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"tip\",\n" +
			"          \"alias\": \"Tip\",\n" +
			"          \"domain\": true,\n" +
			"          \"domain_table\": \"park_tip\",\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 2,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": [\n" +
			"            {\n" +
			"              \"id\": 1,\n" +
			"              \"desc\": \"PARK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 2,\n" +
			"              \"desc\": \"YEŞİL ALAN & REFÜJ\"\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 69,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"alan\",\n" +
			"          \"alias\": \"Alan\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"float\",\n" +
			"          \"position\": 3,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"ilce\",\n" +
			"          \"alias\": \"İlçe\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 4,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"mahalle\",\n" +
			"          \"alias\": \"Mahalle\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 5,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 68,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"touch_by\",\n" +
			"          \"alias\": \"Kullanıcı\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_by\",\n" +
			"          \"position\": 6,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 67,\n" +
			"          \"layer\": \"park\",\n" +
			"          \"field\": \"touch_date\",\n" +
			"          \"alias\": \"Tarih\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_date\",\n" +
			"          \"position\": 7,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        }\n" +
			"      ],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"park\",\n" +
			"      \"alias\": \"Park\",\n" +
			"      \"type\": \"MultiPolygon\",\n" +
			"      \"workspace\": \"kgm\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },\n" +
			"\t{\n" +
			"      \"fields\": [\n" +
			"        {\n" +
			"          \"id\": 378,\n" +
			"          \"layer\": \"yapi\",\n" +
			"          \"field\": \"ad\",\n" +
			"          \"alias\": \"Ad\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"varchar\",\n" +
			"          \"position\": 1,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 377,\n" +
			"          \"layer\": \"yapi\",\n" +
			"          \"field\": \"tip\",\n" +
			"          \"alias\": \"Tip\",\n" +
			"          \"domain\": true,\n" +
			"          \"domain_table\": \"yapi_tip\",\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 2,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": [\n" +
			"            {\n" +
			"              \"id\": 1,\n" +
			"              \"desc\": \"HAVALANDIRMA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 2,\n" +
			"              \"desc\": \"CAMI_MESCID\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 3,\n" +
			"              \"desc\": \"ATM\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 4,\n" +
			"              \"desc\": \"MUHTARLIK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 5,\n" +
			"              \"desc\": \"DEPO\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 6,\n" +
			"              \"desc\": \"CESME\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 7,\n" +
			"              \"desc\": \"BEKCI_KULUBESI\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 8,\n" +
			"              \"desc\": \"ANIT_BUST\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 9,\n" +
			"              \"desc\": \"HAVUZ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 10,\n" +
			"              \"desc\": \"KAFETERYA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 11,\n" +
			"              \"desc\": \"BUFE\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 12,\n" +
			"              \"desc\": \"BINA\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 13,\n" +
			"              \"desc\": \"DOGALGAZ_KUTUSU\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 14,\n" +
			"              \"desc\": \"GUVENLIK_KULUBESI\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 15,\n" +
			"              \"desc\": \"TRAFO\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 16,\n" +
			"              \"desc\": \"TERAS\"\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"\t\t{\n" +
			"          \"id\": 378,\n" +
			"          \"layer\": \"yapi\",\n" +
			"          \"field\": \"alan\",\n" +
			"          \"alias\": \"Alan\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"float\",\n" +
			"          \"position\": 3,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
				"          \"layer\": \"yapi\",\n" +
				"          \"field\": \"ilce\",\n" +
				"          \"alias\": \"Ilce\",\n" +
				"          \"domain\": false,\n" +
				"          \"domain_table\": null,\n" +
				"          \"length\": null,\n" +
				"          \"type\": \"int4\",\n" +
				"          \"position\": 4,\n" +
				"          \"style\": null,\n" +
				"          \"is_required\": false,\n" +
				"          \"permission\": 4,\n" +
				"          \"codedValues\": null\n" +
				"        },\n" +
				"        {\n" +
				"          \"id\": 608,\n" +
				"          \"layer\": \"yapi\",\n" +
				"          \"field\": \"mahalle\",\n" +
				"          \"alias\": \"Mahalle\",\n" +
				"          \"domain\": false,\n" +
				"          \"domain_table\": null,\n" +
				"          \"length\": null,\n" +
				"          \"type\": \"int4\",\n" +
				"          \"position\": 5,\n" +
				"          \"style\": null,\n" +
				"          \"is_required\": false,\n" +
				"          \"permission\": 4,\n" +
				"          \"codedValues\": null\n" +
				"        },\n" +
			"        {\n" +
			"          \"id\": 68,\n" +
			"          \"layer\": \"yapi\",\n" +
			"          \"field\": \"touch_by\",\n" +
			"          \"alias\": \"Kullanıcı\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_by\",\n" +
			"          \"position\": 6,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 67,\n" +
			"          \"layer\": \"yapi\",\n" +
			"          \"field\": \"touch_date\",\n" +
			"          \"alias\": \"Tarih\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_date\",\n" +
			"          \"position\": 7,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        }\n" +
			"      ],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"yapi\",\n" +
			"      \"alias\": \"Yapı\",\n" +
			"      \"type\": \"MultiPolygon\",\n" +
			"      \"workspace\": \"park\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },\n" +
			"\t{\n" +
			"      \"fields\": [],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"mahalle\",\n" +
			"      \"alias\": \"Mahalle\",\n" +
			"      \"type\": \"MultiPolygon\",\n" +
			"      \"workspace\": \"park\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },\n" +
			"\t{\n" +
			"      \"fields\": [],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"ilce\",\n" +
			"      \"alias\": \"İlçe\",\n" +
			"      \"type\": \"MultiPolygon\",\n" +
			"      \"workspace\": \"park\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },\n" +
			"\t{\n" +
			"      \"fields\": [],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"il\",\n" +
			"      \"alias\": \"İl\",\n" +
			"      \"type\": \"MultiPolygon\",\n" +
			"      \"workspace\": \"kgm\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },\n" +
			"    \t\n" +
			"    {\n" +
			"      \"fields\": [\n" +
			"        {\n" +
			"          \"id\": 378,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"cap\",\n" +
			"          \"alias\": \"Cap\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"float\",\n" +
			"          \"position\": 1,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 377,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"tip\",\n" +
			"          \"alias\": \"Tip\",\n" +
			"          \"domain\": true,\n" +
			"          \"domain_table\": \"agac_tip\",\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 2,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": [\n" +
			"            {\n" +
			"              \"id\": 1,\n" +
			"              \"desc\": \"AĞAÇ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 2,\n" +
			"              \"desc\": \"FİDAN\"\n" +
			"            }\n" +
			"            ,\n" +
			"            {\n" +
			"              \"id\": 3,\n" +
			"              \"desc\": \"ÇALILIK\"\n" +
			"            }\n" +
			"                        ,\n" +
			"            {\n" +
			"              \"id\": 4,\n" +
			"              \"desc\": \"DİĞER\"\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"                {\n" +
			"          \"id\": 377,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"tur\",\n" +
			"          \"alias\": \"Tür\",\n" +
			"          \"domain\": true,\n" +
			"          \"domain_table\": \"agac_tur\",\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 3,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": [\n" +
			"            {\n" +
			"              \"id\": 1,\n" +
			"              \"desc\": \"ÇAM\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 2,\n" +
			"              \"desc\": \"KAVAK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 3,\n" +
			"              \"desc\": \"ÇINAR\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 4,\n" +
			"              \"desc\": \"KAVAK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 5,\n" +
			"              \"desc\": \"DİĞER\"\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"yas\",\n" +
			"          \"alias\": \"Yaş\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"in4\",\n" +
			"          \"position\": 4,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"ilce\",\n" +
			"          \"alias\": \"İlçe\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 5,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"mahalle\",\n" +
			"          \"alias\": \"Mahalle\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 6,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 68,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"touch_by\",\n" +
			"          \"alias\": \"Kullanıcı\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_by\",\n" +
			"          \"position\": 7,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 67,\n" +
			"          \"layer\": \"agac\",\n" +
			"          \"field\": \"touch_date\",\n" +
			"          \"alias\": \"Tarih\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_date\",\n" +
			"          \"position\": 8,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        }\n" +
			"      ],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"agac\",\n" +
			"      \"alias\": \"Agaç\",\n" +
			"      \"type\": \"MultiPoint\",\n" +
			"      \"workspace\": \"park\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    },"+
			"\t\n" +
			"    {\n" +
			"      \"fields\": [\n" +
			"        {\n" +
			"          \"id\": 378,\n" +
			"          \"layer\": \"donati\",\n" +
			"          \"field\": \"alan\",\n" +
			"          \"alias\": \"Alan\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"float\",\n" +
			"          \"position\": 1,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 377,\n" +
			"          \"layer\": \"donati\",\n" +
			"          \"field\": \"tip\",\n" +
			"          \"alias\": \"Tip\",\n" +
			"          \"domain\": true,\n" +
			"          \"domain_table\": \"donati_tip\",\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 2,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": true,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": [\n" +
			"            {\n" +
			"              \"id\": 1,\n" +
			"              \"desc\": \"DEKORATİF TAŞ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 2,\n" +
			"              \"desc\": \"ÇALI\"\n" +
			"            }\n" +
			"            ,\n" +
			"            {\n" +
			"              \"id\": 3,\n" +
			"              \"desc\": \"KAYRAKTAŞ\"\n" +
			"            }\n" +
			"                        ,\n" +
			"            {\n" +
			"              \"id\": 4,\n" +
			"              \"desc\": \"ÇİM\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 5,\n" +
			"              \"desc\": \"KİLİTTAŞ\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 6,\n" +
			"              \"desc\": \"KUM\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 7,\n" +
			"              \"desc\": \"MOZAİK\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 8,\n" +
			"              \"desc\": \"TRAVERTER\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 9,\n" +
			"              \"desc\": \"ŞEVTAŞI\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 10,\n" +
			"              \"desc\": \"KAROTAŞ\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 11,\n" +
			"              \"desc\": \"KIRMATAŞ\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 12,\n" +
			"              \"desc\": \"HALI SAHA\"\n" +
			"            }            ,\n" +
			"            {\n" +
			"              \"id\": 13,\n" +
			"              \"desc\": \"TOPRAK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 14,\n" +
			"              \"desc\": \"BETON\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 15,\n" +
			"              \"desc\": \"KÜPTAŞ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 16,\n" +
			"              \"desc\": \"AHŞAP\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 17,\n" +
			"              \"desc\": \"KARETAŞ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 18,\n" +
			"              \"desc\": \"ASFALT\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 19,\n" +
			"              \"desc\": \"PARKE\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 20,\n" +
			"              \"desc\": \"KAUÇUK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 21,\n" +
			"              \"desc\": \"MERMER\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 22,\n" +
			"              \"desc\": \"DİKDÖRTGENTAŞ\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 23,\n" +
			"              \"desc\": \"ÇİÇEKLİK\"\n" +
			"            },\n" +
			"            {\n" +
			"              \"id\": 24,\n" +
			"              \"desc\": \"GRANİT\"\n" +
			"            }\n" +
			"          \n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"donati\",\n" +
			"          \"field\": \"ilce\",\n" +
			"          \"alias\": \"İlçe\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 3,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 608,\n" +
			"          \"layer\": \"donati\",\n" +
			"          \"field\": \"mahalle\",\n" +
			"          \"alias\": \"Mahalle\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"int4\",\n" +
			"          \"position\": 4,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 68,\n" +
			"          \"layer\": \"donati\",\n" +
			"          \"field\": \"touch_by\",\n" +
			"          \"alias\": \"Kullanıcı\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_by\",\n" +
			"          \"position\": 5,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 67,\n" +
			"          \"layer\": \"donati\",\n" +
			"          \"field\": \"touch_date\",\n" +
			"          \"alias\": \"Tarih\",\n" +
			"          \"domain\": false,\n" +
			"          \"domain_table\": null,\n" +
			"          \"length\": null,\n" +
			"          \"type\": \"touch_date\",\n" +
			"          \"position\": 6,\n" +
			"          \"style\": null,\n" +
			"          \"is_required\": false,\n" +
			"          \"permission\": 4,\n" +
			"          \"codedValues\": null\n" +
			"        }\n" +
			"      ],\n" +
			"      \"id\": 305,\n" +
			"      \"layer\": \"donati\",\n" +
			"      \"alias\": \"Donatı\",\n" +
			"      \"type\": \"MultiPolygon\",\n" +
			"      \"workspace\": \"park\",\n" +
			"      \"permission\": 4,\n" +
			"      \"is_required\": false,\n" +
			"      \"style\": null\n" +
			"    }"+
			"  ]\n" +
			"\n" +
			"}";
}
