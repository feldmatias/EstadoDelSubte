<?php

namespace App\SubwayState;


class SubwayState {

    const LINE_PREFIX = 'Linea';

    public function __construct($line, $state) {
        $this->line = $line;
        $this->state = $state;
    }

    public function getState() {
        return $this->state;
    }

    public function getLine() {
        $line = ltrim($this->line, self::LINE_PREFIX);
        return $line[0];
    }
}
