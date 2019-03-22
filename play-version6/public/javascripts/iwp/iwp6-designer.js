// Objects drag and drop menu
var drake = dragula([document.getElementById("objects")]);

// https://www.w3schools.com/howto/howto_js_dropdown.asp
function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}

window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}

/////////////////////////////////////////////////

// Idx in objects arr of clicked object
var clicked_idx = 0;

class Obj {
    constructor (type, name, xpath=null, ypath=null, shape=null,
            width=null, height=null, theta=null) {
        this.type = type;
        this.name = name;
        this.xpath = xpath;
        this.ypath = ypath;
        this.shape = shape;
        this.width = width;
        this.height = height;
        this.theta = theta;
        this.draw_vectors = [];
        this.graphable = [];
    }
}

function addDraggableObject(type) {
    name = type + " " + (objects.length + 1);
    new_obj = new Obj(type, name);
    num_objects = objects.push(new_obj);

    $("#objects").append("<div class='draggable'>" + name + "</div>");
}

$(".draggable").click(function () {
    var element_id = $(this).attr('id');
    var start = element_id.indexOf('_');
    idx = parseInt(element_id.slice(start + 1));
    clicked_idx = idx;
    clicked_obj = objects[clicked_idx];

    // Populate form with object properties
    $("#animation_name").val(clicked_obj.name);

    $("#xpath").val(clicked_obj.xpath);
    $("#ypath").val(clicked_obj.ypath);

    $("#shape").val(clicked_obj.shape);

    $("#widtb").val(clicked_obj.widtb);
    $("#height").val(clicked_obj.height);
    $("#theta").val(clicked_obj.theta);
});

$("#save").click(function() {
    objects[clicked_idx].name = $("#animation_name").val();
    objects[clicked_idx].xpath = $("#xpath").val();
    objects[clicked_idx].ypath = $("#ypath").val();
    objects[clicked_idx].shape = $("#shape").val();

    objects[clicked_idx].width = $("#width").val();
    objects[clicked_idx].height = $("#height").val();
    objects[clicked_idx].theta = $("#theta").val();

    objects[clicked_idx].draw_vectors = [];
    $.each($(".draw_vectors:checked"), function(){
        objects[clicked_idx].draw_vectors.push($(this).val());
    });

    objects[clicked_idx].graphable = [];
    $.each($(".graphable:checked"), function(){
        objects[clicked_idx].graphable.push($(this).val());
    });

    console.log("saved", clicked_idx, objects[clicked_idx]);
});

var obj_0 = new Obj("output", "Time");
var obj_1 = new Obj("output", "Graph Window");
var obj_2 = new Obj("output", "XY Window");
var obj_3 = new Obj("output", "Description");

// List of objects, ordered based on when they are added
// not their order in the drag-and-drop menu
var objects = [obj_0, obj_1, obj_2, obj_3];