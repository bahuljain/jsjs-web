// setting up the editor
var editor = ace.edit("editor");
editor.setTheme("ace/theme/clouds");
editor.getSession().setMode("ace/mode/scala");
editor.$blockScrolling = Infinity

var gcd = multiline(function(){/*
val gcd = /\(a, b) => {
  if a == b then a
  else {
    if a > b
    then gcd((a - b), b)
    else gcd((b - a), a);
  };
};
*/});

var prime = multiline(function(){/*
val countPrimes = /\(n: num): num => {
  val aux = /\(count: num, l: list num): num => {
    if hd(l) * hd(l) <= n
    then aux(count + 1, List.filter(/\(x: num): bool => x % hd(l) != 0, l))
    else count + List.length(l);
  };

  aux(0, List.range(2, n+1));
};
*/});

var bottles = multiline(function(){/*
val toStr = /\(x: num): string => if x == 0 then "no more" else num_to_string(x);

val ninetyNine = /\(x: num): unit => {
  if x == 0
  then {
     print("No more bottles of beer on the wall, no more bottles of beer.");
     print("Go to the store and buy some more, 99 bottles of beer on the wall.");
  }
  else {
     print(toStr(x) ^ "bottle of beer on the wall, " ^
       toStr(x) ^  " bottle of beer.");
     if x == 2
     then {
        print("Take one down and pass it around, " ^
                toStr(x - 1) ^ " bottle of beer on the wall.");
        ninetyNine(x-1);
     }
     else {
        print("Take one down and pass it around, " ^
                toStr(x - 1) ^ " bottles of beer on the wall.");
        ninetyNine(x-1);
     };
  };
};

ninetyNine(99);
*/});


var maps = multiline(function() {/*
// create maps just like javascript
val marksInExams = {
    "physics": 80,
    "mathematics": 83,
    "greek": 63,
    "computer science": 94
};

val moneyOwedByFriends = {
    "ben": 10,
    "mary": 20,
    "mark": 43,
    "alice": 54
};

// a function that takes a map
val getTotal = /\(m: <string: num>): num => {
    val values = Map.values(m);
    List.fold_left(/\(x, y) => x + y, 0, values);
};

print(getTotal(moneyOwedByFriends));
*/});

var samples = {
    gcd: gcd,
    prime: prime,
    bottles: bottles,
    maps: maps
};

var allPanels = $('ul#examples li p').hide();

$("ul#examples li").on('click', function(e) {
    allPanels.slideUp();
    $(this).find('p').slideDown();
    var title = $(this).data('title');
    editor.setValue(samples[title], 1);
});
