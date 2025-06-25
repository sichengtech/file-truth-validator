<?php
define('APP_ID', 'app');
//读取本地配置 .env
$https = false;
$dirPath = dirname( dirname( __FILE__ )  );
if ( file_exists($dirPath. '/.env') ) {
    $env = file_get_contents( $dirPath .'/.env' );
    if ( !empty($env) ) {
        $env = json_decode($env, true);
        if ( is_array($env)
            && isset($env['https'])
            && 'on' == $env['https']
        ) {
            $https = true;
        }
    }
}

$wapConfig = array(
    'domain'    => ( $https ? 'https://':'http://'). 'www.jybd.cn',
    'randomMark' => '20190424'
);
return $wapConfig;
