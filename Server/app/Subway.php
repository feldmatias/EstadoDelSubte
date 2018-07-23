<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use App\Http\Controllers\FirebaseController;

class Subway extends Model
{
    public function store($newState){
    	$oldState = $this->state;

    	$this->timestamps = true;
    	$this->state = $newState;
    	$this->error = false;
    	$this->save();

    	if ($oldState != $this->state){
    		$this->sendNotification();
    	}
    }

    public function storeError(){
    	$this->timestamps = false;
    	$this->error = true;
    	$this->save();
    }

    private function sendNotification(){
    	$data = array(
    		'line' => $this->line,
    		'state'=> $this->state
    	);

    	FirebaseController::sendData($data);
    }
}
