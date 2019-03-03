<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Route::get('/subway/state', 'SubwayController@getSubwaysState');

Route::get('/subway/check', 'SubwayController@check');

Route::get('/subway/refresh', 'SubwayController@refresh');
