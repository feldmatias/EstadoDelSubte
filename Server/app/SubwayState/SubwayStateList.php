<?php

namespace App\SubwayState;


class SubwayStateList {

    public function __construct() {
        $this->states = [];
    }

    public function addState($lineState) {
        $this->states[] = $lineState;
    }

    public function getSubwayState($subway) {
        foreach ($this->states as $state) {
            if (strtolower($state->getLine()) == strtolower($subway->line)) {
                return $state;
            }
        }

        return null;
    }
}
