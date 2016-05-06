let jQuery = require('cash-dom');
let samples = require('./samples');

const SERVER_URL = "http://localhost:8080/compiler";

// setting up the editor
let editor = ace.edit("editor");
editor.setTheme("ace/theme/clouds");
editor.getSession().setMode("ace/mode/scala");
editor.$blockScrolling = Infinity

// initial setup
let allPanels = jQuery('ul#examples li').addClass("closed");

// add event handlers on the examples
jQuery("ul#examples li").on('click', function(e) {
    allPanels.addClass('closed');
    jQuery(this).removeClass("closed");
    let title = jQuery(this).data('title');
    editor.setValue(samples[title], 1);
});

let showOutput = `let console_outputs = [];
var print_me = function(msg) {
    console_outputs.push(msg);
};`;

let return_op =`;(function() { return console_outputs }());`;

let results = jQuery('div#results')[0];

let update_output = function(output, success) {
    console.log(output);
    let str;
    if (success) {
        str = output.map(function(o) { return "<p>" + o + "</p>" }).join("")
    } else {
        str = "<p>" + output + "</p>";
    }
    jQuery(results).html(str);
    if (success) {
        jQuery(results).addClass("success").removeClass("failure");
    } else {
        jQuery(results).addClass("failure").removeClass("success");
    }
};

jQuery("button#run").on('click', function(e) {
    let userCode = editor.getValue();

    // loading
    update_output("Loading ...", false);


    fetch(SERVER_URL, {
        "method": "post",
        "headers": {"Content-type": "application/json"},
        "body": JSON.stringify({"sourceCode": userCode})
    })
    .then(function(response) {
            if (response.status !== 200) {
                update_output("There was an error returned by the server", false);
                return;
            }
            response.json().then(function(data) {
                console.log("code loaded");

                let { status, compiledCode, compilationTime }  = data;

                // compiler error
                if (status === 1) {
                    let { error } = data;
                    update_output(error, false);

                } else {
                    let code = data["compiledCode"];
                    console.log("got this code", code);
                    code = code.replace("// printing utils", showOutput);
                    code += return_op;

                    // TODO: remove from prod
                    window.compiledCode = code;

                    let result = eval(code);
                    update_output(result, true);
                }
            });
        })
    .catch(function(err) {
        update_output("Errer in fetch: " + err, false);
    });

});
