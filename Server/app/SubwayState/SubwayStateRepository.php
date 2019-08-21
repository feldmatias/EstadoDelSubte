<?php

namespace App\SubwayState;

use Ixudra\Curl\Facades\Curl;

class SubwayStateRepository {

    const LANGUAGE = "es";

    public function getSubwayStates() {
        $response = Curl::to($this->getUrl())->returnResponseObject()->get();

        if ($response->status != 200){
            return null;
        }

        return $this->parseSubwayStatesResponse(json_decode($response->content));
    }

    private function parseSubwayStatesResponse($response) {
        $states = new SubwayStateList();

        $entities = $response->entity;
        foreach ($entities as $entity) {
            $alert = $entity->alert;
            $line = $alert->informed_entity[0]->route_id;

            $translations = $alert->description_text->translation;
            foreach ($translations as $translation) {
                if ($translation->language == self::LANGUAGE) {
                    $state = $translation->text;
                }
            }

            $state = new SubwayState($line, $state);
            $states->addState($state);
        }

        return $states;
    }

    private function getUrl() {
        return "https://apitransporte.buenosaires.gob.ar/subtes/serviceAlerts?json=1&client_id=" 
            . config('app.buenosAiresClientId') 
            . "&client_secret=" 
            . config('app.buenosAiresClientSecret');
    }
}
