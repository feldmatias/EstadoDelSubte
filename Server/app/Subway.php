<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use App\Http\Controllers\FirebaseController;

class Subway extends Model
{
    protected $appends = ['status'];

    const normal_states = ["normal", "habitual", "completo"];
    const limited_states = ["limitado", "demora", "no se detienen"];

    const status_normal = "normal";
    const status_closed = "closed";
    const status_limited = "limited";

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

    public function getStatusAttribute(){
        $state = strtolower($this->state);

        foreach (self::normal_states as $status){
            if (strpos($state, $status) !== false){
                return self::status_normal;
            }
        }
        
        foreach (self::limited_states as $status){
            if (strpos($state, $status) !== false){
                return self::status_limited;
            }
        }
        
        return self::status_closed;
    }
}
