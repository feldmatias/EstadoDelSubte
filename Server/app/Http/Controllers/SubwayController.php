<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Subway;
use Sunra\PhpSimple\HtmlDomParser;
use Ixudra\Curl\Facades\Curl;

class SubwayController extends Controller
{

	const url = "https://www.metrovias.com.ar/";

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

    private function storeNewState($web){
        $html = HtmlDomParser::str_get_html($web);
        $subways = Subway::get();
        foreach ($subways as $subway){
            $container = 'span[id=status-line-' . $subway->line . ']'; //format == <span id='status-line-A'>State</span>
            $newState = $html->find($container, 0)->innertext;
            $subway->store($newState);
        }
    }
}
