var page_map = new Map([["Main", {key: "", index: "", href: "Main", target: "", "name": "Main"}]]);

var d_mains = document.querySelectorAll("div.iwp-help-main");
var main_names = get_page_main_names(d_mains);

var main_map = {};
main_names.forEach((name, i) => main_map[name] = d_mains[i]);

var main_name = page;
var d_main = main_map[main_name];
var hidden_name = main_names[1-main_names.indexOf(main_name)];
var d_hidden = main_map[hidden_name];

var show_msg = "Show all";
var hide_msg = "Hide all";






function get_page_main_names(d_mains) {
	var i;
	var names = [];
	for (i = 0; i < d_mains.length; ++i) {
		var name = d_mains[i].classList[1]
		name = name.slice("iwp-help-page-".length);
		names.push(name);
	}
	return names;
}


function activate_pages() {
	d_main.style.display = "block";
	// d_hidden.style.display = "block";
	
	var pgs = children_matches(d_main,"div.iwp-numbered");
	var pg;
	var i;
	for (i = 0; i < pgs.length; ++i) { 
		pg = pgs[i];
		var name = pg.classList[1];
		var index = pg.closest("div.iwp-help-main").classList[1].slice("iwp-help-page-".length)+"_"+name;
		console.log("wow", index);
		pg.classList.add("iwp-help-page-All");
		pg.classList.add("iwp-help-page-"+name);
		if (!(pg.classList.contains("iwp-help-page-"+subpage))) {
			console.log("deactivating this page",pg,subpage)
			pg.classList.add("hidden-but-accessible");
			page_map.get(index)["h_wrap"].classList.add("hidden-but-accessible");
		}
	}
	
	var main = document.querySelector("div.iwp-help-main").innerHTML
	if (main.innerHTML == "") {
		main.innerHTML = "Sorry, but the page you have requested does not exist or has been removed. Click <a href=''>here</a> to return to the main page."
	}
	
}

function activate_links() {
	console.log(page_map);
	
	var as_to_main = document.querySelectorAll("a.iwp-help-link.Main");
	
	var i;
	for (i = 0; i < as_to_main.length; ++i) {
		as_to_main[i].classList.toggle("Main");
		as_to_main[i].classList.add(main_name+"_Main");
	}
	
	var as = document.querySelectorAll("a.iwp-help-link");
	
	
	function check(a, func) {
		var info = page_map.get(a.classList[1]);
		if (info != undefined) {
			func(a,info);
		} else {
			console.log("Oops! couldn't link this one!",a.classList);
		}
		console.log(a,info);
		a.target = info["target"];
	}
	set_all = function(a,info) {
		a.href = "All"+"#"+info["key"];
	}
	set_sgl = function(a,info) {
		a.href = info["href"];
	}
	
	
	if (subpage == "All") {
		for (i = 0; i < as.length; ++i) {
			check(as[i], set_all);
		}
		
	} else {
		for (i = 0; i < as.length; ++i) {
			check(as[i], set_sgl);
		}
	}
}

function listen_to_dropdown() {
	var dropdown = document.getElementsByClassName("dropdown-btn");
	var i;
	for (i = 0; i < dropdown.length; ++i) {
		dropdown[i].addEventListener("click", toggle_drpdwn);
	}
	var dropdown_hb = document.getElementsByClassName("iwp-prdb");
	for (i = 0; i < dropdown_hb.length; ++i) {
		dropdown_hb[i].addEventListener("click", toggle_prdb);
		var dropdown_ha = sibling_matches(dropdown_hb[i], "button.iwp-prdb-showall");
		if (dropdown_ha[0] != undefined) {
			dropdown_ha[0].addEventListener("click", toggle_prdb_showall);
		}
	}
}
function toggle_drpdwn() {
	this.classList.toggle("active");
	toggle_display(this.nextElementSibling);
	toggle_caret(this.querySelector("i"));
}

function toggle_prdb() {
var h_wrap = this.closest("div.iwp-prdd");
	toggle_display(h_wrap.nextElementSibling);
	toggle_caret(this.querySelector("i"));
	var h_wrap_parent = h_wrap.parentNode.previousElementSibling;
	if (h_wrap_parent.matches("div.iwp-prdd") && h_wrap_parent.querySelector("u") != null) {
		console.log(h_wrap_parent);
		var ds = h_wrap_parent.nextElementSibling.querySelectorAll("div.iwp-numbered");
		var u = h_wrap_parent.querySelector("u")
		var all = true;
		var none = true;
		for (var i = 0, d = ds[0]; i < ds.length; ++i, d = ds[i]) {
			if (getComputedStyle(d, null).display == "block") {
				none = false;
			} else {
				all = false;
			}
			if (!(all || none)) {
				break;
			}
		}
		if (none) {
			console.log("none",h_wrap_parent);
			u.innerHTML = show_msg;
			
		} else if (all) {
			console.log("all",h_wrap_parent);
			u.innerHTML = hide_msg;
			
		} else {
			console.log("?!?!?",h_wrap_parent);
			u.innerHTML = show_msg;
			
		}
	}

}
function toggle_prdb_showall(event) {
	event.preventDefault();
	var h_wrap = this.closest("div.iwp-prdd");
	if (!(on_prdb_showall(h_wrap))) {
		off_prdb_showall(h_wrap);
	}
	
}

function on_prdb_showall(h_wrap) {
	var u = h_wrap.querySelector("u")
	if (u.innerHTML == show_msg) {
		var ds = h_wrap.nextElementSibling.querySelectorAll("div.iwp-numbered");
		h_wrap.nextElementSibling.style.display = "block";
		on_caret(h_wrap.querySelector("i"));
		for (var i = 0, d = ds[0]; i < ds.length; ++i, d = ds[i]) {
			console.log(d);
			d.style.display = "block";
			on_caret(d.previousElementSibling.querySelector("i"));
		}
		u.innerHTML = hide_msg;
		return true;
	}
	return false;
}

function off_prdb_showall(h_wrap) {
	var u = h_wrap.querySelector("u")
	if (u.innerHTML == hide_msg){
		var ds = h_wrap.nextElementSibling.querySelectorAll("div.iwp-numbered");
		for (var i = 0, d = ds[0]; i < ds.length; ++i, d = ds[i]) {
			console.log(d);
			d.style.display = "none";
			off_caret(d.previousElementSibling.querySelector("i"));
		}
		u.innerHTML = show_msg;
		return true;
	}
	return false;
}

function toggle_display(elt) {
	if (getComputedStyle(elt, null).display === "block") {
		elt.style.display = "none";
	} else {
		elt.style.display = "block";
	}
}
function toggle_caret(i) {
	if (!(off_caret(i))) {
		on_caret(i);
	}
}
function on_caret(i) {
	if (i.classList.contains("fa-caret-right")) {
		i.classList.remove("fa-caret-right");
		i.classList.add("fa-caret-down");
		return true;
	}
	return false;
}
function off_caret(i) {
	if (i.classList.contains("fa-caret-down")) {
		i.classList.remove("fa-caret-down");
		i.classList.add("fa-caret-right");
		return true;
	}
	return false;
}



function get_info__push_r__page_map(d, depth, rList, big_name) {
	console.log("getting name_href for classList:",d.classList,d);
	var r = d.classList[1];
	var name = r.replace( /-(?=[^-])/g, " ");
	console.log(name);
	
	rList.push(r);
	var index = big_name+"_"+rList.join("_");
	console.log("this is becmoing an index!",index);
	
	var href = rList[0]+"#"+index;
	var target = "";
	if (big_name != main_name) {
		href = url_map[big_name]+href;
		target = "_blank";
	}
	
	page_map.set(index, {"key": index, "index": index, "href": href, "target": target, "name": name});
	return page_map.get(index);
}
function make_b_dropdown(depth, info) {
	var b = document.createElement("button");
	b.classList.add("iwp-toc-h"+String(depth));
	b.classList.add("dropdown-btn");
	b.classList.add("active");
	b.innerHTML = info.name + " <i class=\"fa fa-caret-down\">";
	return b;
}
function make_ba(name, href, cls="") {
	var b = make_b("", cls);
	var a = document.createElement("a");
	a.href = href;
	a.innerHTML = name;
	b.appendChild(a);
	console.log(b);
	return b;
}
function make_b(innerHTML, classList="") {
	return make_el("button", {innerHTML:innerHTML, classList:classList});
}
function make_el(tag, attr_map) {
	var el = document.createElement(tag);
	var i;
	for (var attr in attr_map) {
		el[attr] = attr_map[attr];
	}
	return el;
}

function make_a_from_index(depth, info) {
	return make_el("a", {innerHTML:info.name, classList:"iwp-help-link "+info.index+" iwp-toc-h"+String(depth)});
}
function make_a_from_href(depth, info) {
	var a = document.createElement("a");
	a.classList.add("iwp-toc-h"+String(depth));
	a.innerHTML = info.name;
	a.href = info.href;
	return a;
}
function make_li(depth, info) {
	li = document.createElement("li");
	li.appendChild(make_a_from_index(depth, info));
	return li;
}
function make_d__push_dhd(depth, dList, dhList, i) {
	var d = document.createElement("div");
	d.classList.add("iwp-toc-h"+String(depth));
	dList.push(d);
	console.log("children",children_matches(l(dhList)[i],"div.iwp-numbered"));
	dhList.push(children_matches(l(dhList)[i],"div.iwp-numbered"));
	return d;
}
function make_u__push_ud(depth, uList, dhList, i) {
	var u = document.createElement("ul");
	u.classList.add("iwp-toc-h"+String(depth));
	uList.push(u);
	console.log(l(dhList)[i]);
	dhList.push(children_matches(l(dhList)[i],"div.iwp-numbered"));
	return u;
}
function format_hab(h, info, more_bool) {
	format_ha(h, info);
	var d = h.appendChild(make_el("div",{classList:"iwp-prdh-dwab"}));
	if (more_bool) {
		var b = d.appendChild(make_el("button",{classList:"iwp-prdb-showall"}));
		b.appendChild(make_el("u",{innerHTML:show_msg}));
	}
	d.appendChild(make_b("<i class=\"fa fa-caret-down\">","iwp-prdb"));
	// d.style.fontSize = Math.floor(parseFloat(getComputedStyle(h, null).fontSize) / 2) + "px";
}
function format_ha(h, info) {
	var a = document.createElement("a");
	a.classList.add("anchor");
	console.log(info);
	if (l(info.index) == "_") {
		info.index = info.index.slice(0,-1);
	}
	a.name = info.index;
	h.before(a);
}
function make_h__page_map(depth, info, dh, more_bool) {
	var h_wrap = make_el("div",{classList:"iwp-prdd iwp-numbered-h"+String(depth)});
	h_wrap.appendChild(make_el("hr",{classList:"iwp-help-hr-"+String(depth)}));
	var h = h_wrap.appendChild(make_el("h"+String(depth),{innerHTML:"<div class='iwp-prdh-dwh'>"+info.name+"</div>",classList:"iwp-numbered"}));
	
	dh.before(h_wrap);
	page_map.get(info.index)["h_wrap"] = h_wrap;
	
	if (depth == 1) {
		format_ha(h, info);
	} else {
		format_hab(h, info, more_bool);
	}
	if ((dh.classList.contains("hidden-h"))) {
		h_wrap.classList.add("hidden-but-accessible");
	}
	
	return;
}
function pop_these(list) {
	var i;
	for (i = 0; i < list.length; ++i) {
		list[i].pop();
	}
}
function l(list, distance=1) {
	return list[list.length-distance];
}
function wrap(el, wrapper) {
	el.parentNode.insertBefore(wrapper, el);
	wrapper.appendChild(el);
}




function get_toc_in_text(big_name) {
	console.log("working on toc for",big_name);
	function rc_toc_in_text() {
		if (l(dhList).length == 0) {
			return;
		}
		var i;
		for (i = 0, ++depth; i < l(dhList).length; ++i, pop_these([rList,uList,dhList])) {
			var info = get_info__push_r__page_map(l(dhList)[i], depth, rList, big_name);
			u = make_u__push_ud(depth, uList, dhList, i);
			l(uList).appendChild(make_li(depth, info));
			l(uList,2).appendChild(u);
			rc_toc_in_text();
		}
		--depth;
	}
	
	var toc = document.createElement("div");
	toc.classList.add("iwp-toc-in-text");
	
	
	var depth = 0;
	var rList = [];
	var uList = [toc];
	var dhList = [children_matches(main_map[big_name],"div.iwp-numbered")];
	
	rc_toc_in_text();
	return toc;
}
function load_toc_in_text() {
	var i, j;
	for (j = 0; j < main_names.length; ++j) {
		var tocs = d_mains[j].querySelectorAll("div.iwp-toc-container.toc-in-text")
		console.log("tocs:",tocs);
		for (i = 0; i < tocs.length; ++i) {
			tocs[i].appendChild(get_toc_in_text(main_names[j]));
		}
	}
}



function get_toc_sidenav(big_name) {
	function rc_toc_sidenav() {
		if (l(dhList).length == 0) {
			return;
		}
		var i;
		for (i = 0, ++depth; i < l(dhList).length; ++i, pop_these([rList,dList,dhList])) {
			var dh = l(dhList)[i];
			var info = get_info__push_r__page_map(dh, depth, rList, big_name);
			console.log("???", info.name);
			
			
			var d = make_d__push_dhd(depth, dList, dhList, i);
			d.classList.add("dropdown-container");
			if (l(dhList).length > 0) {
				make_h__page_map(depth, info, dh, true);
				
				
				var b = make_b_dropdown(depth, info);
				console.log("moving this info in!", info,"result:",b);
				l(dList,2).appendChild(b);
				
				if (depth >= max_visible_depth) {
					b.classList.remove("active");
					var i_ = b.querySelector("i");
					i_.classList.remove("fa-caret-down");
					i_.classList.add("fa-caret-right");
					d.style.display = "none";
				}
				
			} else {
				make_h__page_map(depth, info, dh, false);
				l(dList,2).appendChild(make_a_from_index(depth, info));
			}
			l(dList,2).appendChild(d);
			rc_toc_sidenav();
		}
		--depth;
	}
	
	
	var toc = document.createElement("div");
	toc.classList.add("iwp-toc-sidenav")
	var toc_div = document.createElement("div");
	toc_div.classList.add("iwp-toc");
	toc.appendChild(toc_div);
	
	
	var max_visible_depth = 3;
	var name, href;
	var d;
	var depth = 0;
	var rList = [];
	var dList = [toc_div];
	var dhList = [children_matches(main_map[big_name],"div.iwp-numbered")];
	 
	
	toc_div.appendChild(make_pretoc(), toc_div.firstChild);
	
	var p = document.createElement("p");
	p.innerHTML = "Table of Contents";
	toc_div.appendChild(p);
	
	
	rc_toc_sidenav();
	return toc;
}

function load_toc_sidenav() {
	document.querySelector("div.iwp-toc-container.toc-sidenav").appendChild(get_toc_sidenav(main_name));
}

function make_pretoc() {
	function make_d_paired() {
		var d = document.createElement("div");
		d.classList.add("iwp-paired-container");
		d_container.appendChild(d);
		return d;
	}
	
	var d_container = document.createElement("div");
	d_container.classList.add("iwp-help-pretoc")
	
	var d = make_d_paired();
	d.classList.add("iwp-pretoc-top");
	make_paired_btn(d, make_ba(main_names[0], url_map[main_names[0]], "iwp-pretoc-btn"), make_ba(main_names[1], url_map[main_names[1]], "iwp-pretoc-btn"));
	
	var i, bs;
	console.log(d);
	bs = d.querySelectorAll("button");
	for (i = 0; i < 2; ++i) {
		var a = bs[i].querySelector("a");
		if (a.innerHTML == main_name) {
			bs[i].classList.add("active");
			console.log(bs, main_name);
		}
		if (subpage == "All") {
			a.href = a.href + "All";
		}
	}
	
	
	var d = make_d_paired();
	var as = make_links_extent();
	make_paired_btn(d, make_ba("Full Page", as[0], "iwp-pretoc-btn"), make_ba("Single Page", as[1], "iwp-pretoc-btn"));
	bs = d.querySelectorAll("button");
	if (subpage == "All") {
		bs[0].classList.add("active");
	} else {
		bs[1].classList.add("active");
	}
	
	
	return d_container;
}

function make_paired_btn(d_container, b1, b2) {
	var bs = [b1, b2];
	var i;
	for (i = 0; i < 2; ++i) {
		var d = document.createElement("div");
		d.classList.add("iwp-paired");
		d.appendChild(bs[i]);
		d_container.appendChild(d);
		
	}
	return d_container;
}

function make_links_extent() {
	var hash = window.location.hash;
	if (subpage == "All") {
		var h1;
		if (hash == "") {
			h1 = "Main";
		} else if (hash.includes("_")) {
			var i = hash.indexOf("_");
			h1 = hash.slice(1,i);
		} else {
			h1 = hash.slice(1);
			hash = "";
		}
		var as = [make_a_from_href(0, {name:"Full Page", href:window.location.href}), make_a_from_href(0, {name:"Single Page", href:h1+hash})];
	} else {
		console.log("hash for full page link:",hash);
		if (hash == "") {
			hash = "#"+window.location.href.substring(window.location.href.lastIndexOf("/")+1);
		}
		console.log("hash for full page link:",hash);
		var as = [make_a_from_href(0, {name:"Full Page", href:"All"+hash}), make_a_from_href(0, {name:"Single Page", href:window.location.href})];
		console.log("link:",as[1].href);
	}
	return as;
}
var children_matches = function (elem, selector) {
	return Array.prototype.filter.call(elem.children, function (child) {
		return child.matches(selector);
	});
};
var sibling_matches = function (elem, selector) {
	return children_matches(elem.parentNode, selector);
}


load_toc_in_text();
load_toc_sidenav();
// insert_dividers([1,2,3,4]);
listen_to_dropdown();
activate_pages();
activate_links();

