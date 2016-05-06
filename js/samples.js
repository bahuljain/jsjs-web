/* set of JSJS code samples */

let gcd = `val gcd = /\\(a, b) => {
  // nested if-else expressions
  if a == b then a
  else if a > b
  then gcd((a - b), b)
  else gcd((b - a), a);
};

print(gcd(10, 24));`;

let prime = `val countPrimes = /\\(n: num): num => {
    val aux = /\\(count, l) => {
        if hd(l) * hd(l) <= n
        then aux(count + 1, List.filter(/\\(x) => x % hd(l) != 0, l))
        else count + List.length(l);
    };
    aux(0, List.range(2, n+1));
};

// should be 25
print(countPrimes(100));`;

let bottles = `val toStr = /\\(x: num): string => if x == 0 then "no more" else num_to_string(x);

val ninetyNine = /\\(x) => {
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

ninetyNine(99);`;


let maps = `// create maps just like javascript
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
val getTotal = /\\(m: <string: num>): num => {
    val values = Map.values(m);
    List.fold_left(/\\(x, y) => x + y, 0, values);
};

print(getTotal(moneyOwedByFriends));`;

module.exports = {
    gcd: gcd,
    prime: prime,
    bottles: bottles,
    maps: maps
};
