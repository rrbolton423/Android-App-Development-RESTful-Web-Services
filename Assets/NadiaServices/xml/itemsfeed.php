<?php

require_once('../database/itemsquery.php');

//Build array of data items
$arRows = array();
while ($row = mysqli_fetch_assoc($rsItems)) {
  array_push($arRows, $row);
}

// build root XML element
$itemsElement = new SimpleXMLElement('<items></items>');

// loop data and build data structure
for ($i = 0; $i < count($arRows); $i++) {
  $itemElement = $itemsElement->addChild('item');
  $itemRow = $arRows[$i];
  
  $itemElement->addChild('itemName', $itemRow['itemName']);
  $itemElement->addChild('category', $itemRow['category']);
  $itemElement->addChild('description', $itemRow['description']);
  $itemElement->addChild('sort', $itemRow['sort']);
  $itemElement->addChild('price', $itemRow['price']);
  $itemElement->addChild('image', $itemRow['image']);
}

mysqli_free_result($rsItems); 

//format for pretty printing
$dom = new DOMDocument('1.0', 'UTF-8');
$dom->preserveWhiteSpace = false;
$dom->formatOutput = true;
$dom->loadXML($itemsElement->asXML());
 
//Send to browser
header('Content-type: text/xml');
echo $dom->saveXML();

?>
