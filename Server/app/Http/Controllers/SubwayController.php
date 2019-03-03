<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Subway;
use Sunra\PhpSimple\HtmlDomParser;
use Ixudra\Curl\Facades\Curl;
use Carbon\Carbon;

class SubwayController extends Controller
{
    //From metrovias page
    /*
    const url = "https://www.metrovias.com.ar";
    private function storeNewState($web){
        $html = HtmlDomParser::str_get_html($web);
        $subways = Subway::get();
        foreach ($subways as $subway){
            $container = 'span[id=status-line-' . $subway->line . ']'; //format == <span id='status-line-A'>State</span>
            $newState = $html->find($container, 0)->innertext;
            $subway->store($newState);
        }
    }
    */

    //From Buenos Aires page

    const url = "https://www.buenosaires.gob.ar/subte";
    private function storeNewState($web){
        $html = HtmlDomParser::str_get_html($web);

        $subways = Subway::get();
        foreach ($subways as $subway){
            $container = 'div[id=linea' . $subway->line . ']'; //format == <div id="lineaA"> ... <span class="msg">State</span>
            $infoContainer = 'span[class=msg]';
            $newState = $html->find($container, 0)->find($infoContainer, 0)->plaintext;
            $subway->store($newState);
        }
    }



    public function getSubwaysState(){
        $subways = Subway::select('line', 'state', 'error', 'updated_at')->get();
        return json_encode($subways);
    }

    public function check(){
        $response = Curl::to(self::url)->returnResponseObject()->get();

        if ($response->status != 200){
            return $this->pageError();
        }
        
        try {
            $this->storeNewState($response->content);   
        } catch (\Throwable $ex) {
            $this->pageError();
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
