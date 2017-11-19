<html>
  <head>
    <title>Alternative Facts</title>
    <link rel="stylesheet" type="text/css" href="style.css">
  </head>
  <body>
    <h1>Alternative Facts Generator</h1>
    <?php
      $filename = "./facts.txt";
      $lines = file($filename, FILE_IGNORE_NEW_LINES);

    for ($i=0; $i < count($lines); $i = $i + 3) {
      // get facts 3 at a time
      echo '
      <div class="container">
        <div class="facts">
          <h3 class="fact1">
            Did you know ' + $lines[$i] + '?
          </h3>
          <h3 class="fact2">
          Did you know ' + $lines[$i+1] + '?
          </h3>
          <h3 class="fact3">
          Did you know ' + $lines[$i+2] + '?
          </h3>
        </div>
      </div>
      ';
    }
    ?>
  </body>
</html>
