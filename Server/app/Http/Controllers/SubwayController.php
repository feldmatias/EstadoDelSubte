<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Subway;
use Sunra\PhpSimple\HtmlDomParser;

class SubwayController extends Controller
{

	const url = "https://www.metrovias.com.ar/";

    public function getSubwaysState(){
    	$this->check();
    	$subways = Subway::select('line', 'state', 'error', 'updated_at')->get();
    	return json_encode($subways);
    }

    public function check(){
    	$web = $this->getHtmlFromUrl();
    	$html = HtmlDomParser::str_get_html($web);
    	$subways = Subway::get();
    	foreach ($subways as $subway){
    		if ($html){
    			$container = 'span[id=status-line-' . $subway->line . ']'; //format == <span id='status-line-A'>State</span>
    			$newState = $html->find($container, 0)->innertext;
    			$subway->store($newState);
    		} else {
    			$subway->storeError();
    		}
    	}
    }

    private function getHtmlFromUrl(){
    	$curl = curl_init();
		curl_setopt($curl, CURLOPT_URL, self::url);
		curl_setopt($curl, CURLOPT_FAILONERROR, true);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		$web = curl_exec($curl);
		if (curl_error($curl)){
		    $web = '';
		}
		curl_close($curl);
		return $web;
    }
}
