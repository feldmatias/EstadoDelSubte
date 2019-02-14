<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Subway;
use Sunra\PhpSimple\HtmlDomParser;
use Ixudra\Curl\Facades\Curl;

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
            $newState = $html->find($container, 0)->find($infoContainer, 0)->innertext;
            $subway->store($newState);
        }
    }


    public function getSubwaysState(){
    	$this->check();
    	$subways = Subway::select('line', 'state', 'error', 'updated_at')->get();
    	return json_encode($subways);
    }

    public function check(){
        $response = Curl::to(self::url)->returnResponseObject()->get();

        if ($response->status != 200){
            return $this->pageError();
        }

        $this->storeNewState($response->content);
    }

    private function pageError(){
    	$subways = Subway::get();
        foreach ($subways as $subway){
            $subway->storeError();
        }
    }

}
