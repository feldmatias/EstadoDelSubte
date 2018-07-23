<?php

namespace App\Http\Controllers;

class FirebaseController{

	const FIREBASE_API_ACCESS_KEY = "******";

	public static function sendData($data){
		$fields = array(
  			"to" => "/topics/all_devices",
  			"data" => $data
		);

		$headers = array(
        	'Authorization: key=' . FirebaseController::FIREBASE_API_ACCESS_KEY,
        	'Content-Type: application/json'
    	);

		$ch = curl_init();
	    curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
	    curl_setopt($ch, CURLOPT_POST, true);
	    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	    curl_setopt($ch ,CURLOPT_RETURNTRANSFER, true);
	    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	    curl_setopt($ch ,CURLOPT_POSTFIELDS, json_encode($fields));
	    curl_exec($ch);
	    curl_close($ch);
	}
}
