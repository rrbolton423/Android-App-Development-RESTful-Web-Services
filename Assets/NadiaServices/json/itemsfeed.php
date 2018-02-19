<?php

require_once('../database/itemsquery.php');

//Build array of data items
$arRows = array();
while ($row_rsItems = mysqli_fetch_assoc($rsItems)) {
  array_push($arRows, $row_rsItems);
}

//Serialize and deliver as JSON
header('Content-type: application/json');
echo json_encode($arRows);

?>
