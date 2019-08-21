<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Subway;
use App\SubwayState\SubwayStateRepository;
use Sunra\PhpSimple\HtmlDomParser;
use Carbon\Carbon;

class SubwayController extends Controller
{
    
    public function getSubwaysState(){
        $subways = Subway::select('line', 'state', 'error', 'updated_at')->get();
        return json_encode($subways);
    }

    public function check(){
        $states = (new SubwayStateRepository())->getSubwayStates();

        if (!$states) {
            $this->pageError();
            return;
        }

        $subways = Subway::get();
        foreach ($subways as $subway){
            if ($state = $states->getSubwayState($subway)) {
                $subway->store($state->getState());
            } else {
                $subway->setNormalState();
            }
        }
    }

    public function refresh(){
        $this->check();
        return $this->getSubwaysState();
    }

    private function pageError(){
    	$subways = Subway::get();
        foreach ($subways as $subway){
            $subway->storeError();
        }

        $lastSubway = $subways->last();
        if ($lastSubway->updated_at < Carbon::now()->subDays(1)){
            $this->sendErrorMail();
            $lastSubway->updated_at = Carbon::now();
            $lastSubway->save();
        }
    }

    private function sendErrorMail(){
        $mail = "feldmatias@gmail.com";
        $subject = "Estado del subte sin reportar";
        $message = "El estado del subte no se puede obtener desde hace un dia.";
        $headers = "From:" . $mail;
        mail($mail, $subject, $message, $headers);
    }

}
