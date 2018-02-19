<?php 

//build query
$sql = "SELECT * FROM dataItems";

//Add category filter
if (isset($_GET['category']))
  $category = $_GET['category'];
else if (isset($_POST['category']))
  $category = $_POST['category'];

if (isset($category)) {
  $filter[] = " category = '$category'";
}
// end of category filter

// create additional filters here 

if (!empty($filter)) {
  $sql .= ' WHERE ' . implode(' AND ', $filter);
}

//Determine sort order
if (isset($_GET['orderby']))
  $orderby = $_GET['orderby'];
else if (isset($_POST['orderby']))
  $orderby = $_POST['orderby'];

//default sort order to itemName column
if (isset($orderby)) {
  $sql .= " ORDER BY " . $orderby;
} else {
  $sql .= " ORDER BY itemName";
}

require_once('connection.php');
$rsItems = mysqli_query($connection, $sql) or 
  die(mysqli_error($connection));

?>