$(document).ready(j);
function j() {
	var p = $("#searKey"), q = $(".search:first"), k = $(".search"), w = $(".sys_mes"), f = $(".sys_mes_all"), e = $(".sys_mes input[type='submit']"), h = $(".new_dub"), g = $(".new_dub input[type='submit']"), s = $(".new_dub input[type='reset']"), n = $("#resultPic"), l = $("#upAudio"), x = $("#bCast"), B = $("#sMeg"), v = $(".textbit140"), t = $("#data_from"), r = $("#data_to"), y = $("#filePic"), c = $(".count_bit"), d = $(".textbit50"), v = $(".textbit40"), z = $(".textbit70"), a = $(".textbit140"), o = $(".trend_form"), C = $(".tredning_data");
	var vReasonInput = $(".plusV input[type='submit']");
	if (p.val() == "") {
		k.addClass("sk");
	}
	q.click(function() {
		p.focus();
	});
	p.live("focus", function() {
		k.removeClass("sk");
	});
	p.live("blur", function() {
		if (this.value == "") {
			k.addClass("sk");
		}
	});
	if (w.length > 0) {
		x.change(function() {
			if (x.attr("checked") == "checked") {
				B.attr({
					disabled : "disabled",
					value : "all"
				});
			} else {
				B.removeAttr("disabled");
				B.attr("value", "");
			}
		});
		if (f) {
			broadcastCheck();
			f.live("submit", function() {
				var I = {}, F = "1";
				f.contents().each(function() {
					var J = this.name;
					if (J) {
						if (this.value == "") {
							F = 0;
							alert("Please finish " + J);
							return false;
						} else {
							I[J] = this.value;
						}
					}
				});
				if (externalParaCheck() == false)
					return false;
				$(".txt_para").each(function() {
					var J = this.name;
					if (J) {
						if (this.value != "") {
							I[J] = this.value;
						}
					}
				});

				if (F == 1) {
					if (confirm("Confirm Send?")) {
						var E = f.attr("action"), G, H, D = $(".suffix");
						if (D.length > 0) {
							G = "json";
							H = function() {
							};
						} else {
							G = "";
							H = function() {
								showMes(true, "Sent Successfully");
								setTimeout(function() {
									window.location.reload();
								}, 1000);
							};
						}
						$.ajax({
							type : "post",
							data : I,
							url : E,
							dataType : G,
							success : function(K) {
								H();
								var J = K.missionKey || "";
								$("#missionKey").val(J);
								broadcastCheck();
							}
						});
					} else {
						return false;
					}
				}
				return false;
			});
		} else {
			w
					.live(
							"submit",
							function() {
								if (v.val() == "") {
									alert("Please edit a message.");
									return false;
								}
								if (B.val() == "") {
									alert("Please enter your username.");
									return false;
								}
								if (B.attr("disabled") == "disabled") {
									if (confirm("Do you want you send this message to all the users?")) {
										return true;
									} else {
										return false;
									}
								}
							});
		}
	}
	if (t.length > 0) {
		t.datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			numberOfMonths : 1,
			onClose : function(D) {
				r.datepicker("option", "minDate", D);
			}
		});
		r.datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			numberOfMonths : 1,
			onClose : function(D) {
				t.datepicker("option", "maxDate", D);
			}
		});
	}
	if (o.length > 0) {
		C.datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			numberOfMonths : 1,
			onClose : function(D) {
			}
		});
		o.live("submit", function() {
			var F = {}, D;
			var E = function(G) {
				$.ajax({
					type : "post",
					data : G,
					url : "/trendingDub/addTrendingDub",
					dataType : "json",
					success : function(H) {
						alert(H.msg);
						if (H.status == 1) {
							window.location.reload();
						}
					}
				});
			};
			o.contents().each(function() {
				var G = this.name;
				if (G) {
					if (this.value == "") {
						alert("Please finish " + this.name);
						D = 0;
						return false;
					} else {
						F[G] = this.value;
						D = 1;
					}
				}
			});
			if (D == 1) {
				if (!confirm("Are you sure to Top this dub?")) {
					return false;
				}
				F.dtime = (new Date(F.Day + " " + F.time)).getTime();
				delete F.Day;
				delete F.time;
				E(F);
				D = 0;
			}
			return false;
		});
	}
	y.change(function() {
		upFile("img", "filePic", "resultPic");
	});
	l.change(function() {
		upFile("mp3", "upAudio", "reAudio");
	});
	if (s.length > 0) {
		s.live("click", function() {
			n.attr("src", "/images/default.jpg");
			c.html("0/70").removeClass("red");
			g.removeAttr("disabled").css({
				background : "",
				color : ""
			});
		});
	}
	if (c.length > 0) {
		z.keydown(function() {
			m(this, 70);
		});
		d.keydown(function() {
			m(this, 50);
		});
		a.keydown(function() {
			m(this, 140);
		});
		v.keydown(function() {
			m(this, 40);
		});
		var m = function(E, D) {
			setTimeout(function() {
				var F = E.value.length, G = $(E).nextAll("p.count_bit").eq(0);
				if (D == 40) {
					sbBt = vReasonInput;
				} else {
					sbBt = e.length > 0 ? e : g;
				}
				if (D > F || D == F) {
					G.removeClass("red");
					if (!c.hasClass("red")) {
						sbBt.removeAttr("disabled").css({
							background : "",
							color : ""
						});
					}
				} else {
					G.addClass("red");
					sbBt.attr("disabled", "disabled").css({
						background : "#eee",
						color : "#999"
					});
				}
				G.html(F + "/" + D);
			}, 200);
		};
	}
	if (h.length > 0) {
		h
				.submit(function() {
					if (l.val() == "") {
						alert("Select a MP3 file less than 3m and shorter than 60 secs .");
						return false;
					}
				});
	}
	$("#display-progress").click(function() {
		$(".progress-bar").toggle();
	});
	formCfm = ({
		init : function() {
			var D = $("form[confm]");
			D.live("submit", function() {
				var E = $(this).attr("confm");
				if (!confirm(E)) {
					return false;
				}
			});
			return this;
		}
	}.init());
	var b = ({
		init : function() {
			this.countWords();
			return this;
		},
		countWords : function() {
			var E = $("textarea[max]"), D = $(".txtsize");
			if (E.length > 0) {
				E
						.live(
								"keyup",
								function() {
									var H = this.value.length, F = $(this)
											.attr("max"), G = $(this).nextAll(
											".txtsize").eq(0), I = $(this)
											.closest("form")
											.find(
													"input[type=submit],a[submit],button[submit]");
									if (H > F) {
										G.addClass("red");
									} else {
										G.removeClass("red");
									}
									G.html(H + "/" + F);
									if (!D.hasClass("red")) {
										I.removeAttr("disabled").removeAttr(
												"id");
									} else {
										I.attr({
											disabled : "disabled",
											id : "bt_dis"
										});
									}
								});
			}
		}
	}.init());
	var u = ({
		init : function() {
			var E = $("#upAudio"), F = $("#upPic"), G = $("#upPool"), D = $(".post");
			G.live("change", function() {
				u.upFile("csv", "upPool", "upPool");
			});
			return this;
		},
		upFile : function(M, D, L) {
			var E = document.getElementById(D), H = document.getElementById(D).files[0], K = new FileReader(), J, O = document
					.getElementById(L);
			if (M == "mp3") {
				var F = "audio/mp3", I = "Mp3 format only..", G = "Audio files should be under 3MB.", N = "", J = 3145728;
			}
			if (M == "img") {
				var F = "image/jpeg", I = "Jpg format only.", G = "Photos should be under 1MB.", N = "./img/up_photo.jpg", J = 1048576;
			}
			if (M == "csv") {
				var F = "application/vnd.ms-excel", I = "Please upload csv file.", G = "File should be under 600K.", N = "", J = 614400;
			}
			K.onload = function(P) {
				if (H.type != F) {
					alert(I);
					E.value = "";
					O.src = N;
					return false;
				}
				if (H.size > J) {
					alert(G);
					E.value = "";
					O.src = N;
					return false;
				}
				O.src = this.result;
			};
			K.readAsDataURL(H);
		}
	}.init());

	/* push type */
	var push_type = $('.push_type');
	if (push_type.length > 0) {

		push_type.live('change', function() {
			var onv = $(this).val(), options = $(this).find('option');

			options.each(function() {
				if ($(this).attr('value') == onv) {
					var showtg = $(this).attr('show');
					showarray = showtg.split(',');

					$('.push_tag,.push_dubid,.push_uid').hide();
					$('.push_tag,.push_dubid,.push_uid').find('input').attr(
							'necess', '');
					for (i in showarray) {
						$('.push_' + showarray[i]).show();
						$('.push_' + showarray[i]).find('input').attr('necess',
								'2');
					}
				}
			})
		})

	}

	/* 表单判断 */
	var formCheck = ({
		init : function() {
			this.necess();
			return this;
		},
		necess : function() {
			var form = $('form'), returned;
			if (form.length > 0) {
				form
						.live(
								'submit',
								function() {
									var nec = $(this).find(
											'input[necess],textarea[necess]');
									nec
											.each(function() {
												var reg = $(this)
														.attr('necess'), value = $(
														this).val();

												if (reg != undefined) {
													switch (reg) {
													case '1':
														if (value == '') {
															alert('Required field cannot be left blank！')
															$(this).focus();
															returned = false
															return false
														} else {
															returned = true
														}
														break;

													case '2':
														if (value
																.match(/^\d*$/g) == null
																|| value
																		.match(/^\d*$/g) == '') {
															alert('Please enter the right number！');
															$(this).focus();
															returned = false
															return false
														} else {
															returned = true
														}
														break;

													default:
														break;
													}
												}

											})
									return returned
								})
			}
		}

	}.init())

	/* u_head album show bigPic */
	bigPic = ({

		init : function() {
			this.check('.album,.u_head');
			return this;
		},

		check : function(objClass) {
			var doc = document, body = $('body'), obj = $(objClass) || {}, imgShell = doc
					.createElement("div"), img = doc.createElement("img");
			imgShell.id = 'bigPic';

			obj.live('click', function() {
				soc = $(this).attr('src'), imgShell = $(imgShell);
				img.src = soc;
				imgShell.css({
					'marginTop' : -img.height / 2,
					'marginLeft' : -img.width / 2
				}).addClass('floatImg_sel');

				imgShell.html(img.outerHTML);
				if ($('#bigPic').length > 0) {
					imgShell.show();
				} else {
					body.append(imgShell);
				}

				var objBind = $('body,#bigPic');
				objBind.live('click', function() {
					imgShell.hide();
					objBind.die();
				})

			})

		}

	}.init())

}
function upFile(k, a, h) {
	var d = document.getElementById(a).files[0];
	var g = new FileReader();
	if (k == "mp3") {
		var b = "audio/mp3", f = 3145728, e = "Mp3 format only.", c = "Audio files should be under 3MB.", l = "";
	}
	if (k == "img") {
		var b = "image/jpeg", f = 1048576, e = "Jpg format only.", c = "Photos should be under 1MB.", l = "./images/default.jpg";
	}
	g.onload = function(n) {
		var m = document.getElementById(h);
		if (d.type != b) {
			alert(e);
			document.getElementById(a).value = "";
			m.src = l;
			return false;
		}
		if (d.size > f) {
			alert(c);
			document.getElementById(a).value = "";
			m.src = "";
			return false;
		}
		m.src = this.result;
	};
	g.readAsDataURL(d);
}
function getData(d, f) {
	var c = $("#data_from"), b = $("#data_to");
	if (c.val() == "" || b.val() == "") {
		event.preventDefault();
	} else {
		var e = (new Date(c.val() + " 00:00:00")).getTime(), a = (new Date(b
				.val()
				+ " 23:59:59")).getTime();
		$("." + d).val(e);
		$("." + f).val(a);
	}
}
function showMes(c, b, a) {
	if (c == true) {
		if (b == undefined) {
			b = "Done.";
		}
		var d = "alertMeg";
	} else {
		if (b == undefined) {
			b = "Oops,operation failed";
		}
		var d = "alertMeg_err";
	}
	if (a == 1) {
		alert(b);
	} else {
		$("body").append("<div class='" + d + "'>" + b + "!</div>");
		setTimeout(function() {
			$("div[class^='alert']").remove();
		}, 1000);
	}
}
function cTime(b) {
	var a = $("." + b);
	for (i = a.length; i--;) {
		gT = a.eq(i).html(), cT = new Date(gT * 1);
		a.eq(i).html(
				cT.getFullYear() + "/" + (cT.getMonth() + 1) + "/"
						+ cT.getDate() + " " + cT.getHours() + ":"
						+ cT.getMinutes() + ":" + cT.getSeconds());
	}
}

function cDate(b) {
	var a = $("." + b);
	for (i = a.length; i--;) {
		gT = a.eq(i).html(), cT = new Date(gT * 1);
		a.eq(i).html(
				cT.getFullYear() + "/" + (cT.getMonth() + 1) + "/"
						+ cT.getDate());
	}
}
function getDur(a) {
	if (a.duration > 60) {
		alert("Audio files should be under 60 seconds long.");
		a.src = "";
		$("#upAudio")[0].value = "";
		return false;
	} else {
		$("#soundDuration").val(a.duration);
	}
}
function externalParaCheck() {
	flag = true;
	var type = $("#pushType").val();
	switch (type) {
	case "16": {
		if ($("#pushUid").val() == "") {
			alert("Please finish push uid");
			flag = false;
		}
		break;
	}
	case "17": {
		if ($("#tagId").val() == "") {
			alert("Please finish tagId");
			flag = false;
		}
		break;
	}
	case "18": {
		if ($("#dubId").val() == "") {
			alert("Please finish dubId");
			flag = false;
		}
		if ($("#pushUid").val() == "") {
			alert("Please finish pushUid");
			flag = false;
		}
		break;
	}
	default:
		;
	}
	return flag;
}
function broadcastCheck() {
	var d = $("#missionKey").val(), h = $(".progress-bar"), f = $(".progress-bar span"), k = $(".progress-bar p"), e = $(".sys_mes_all textarea"), a = $(".sys_mes input[type='submit']"), l = $(".textbit140"), g = $(".textbit50"), b = $("#sysMegDone");
	$.ajax({
		type : "get",
		url : "/messagePush/checkIfHasMission",
		success : function(m, n) {
			if (m == "true") {
				a.attr("disabled", "disabled").css({
					background : "#eee",
					color : "#999"
				});
				e.attr("disabled", "disabled");
				h.show();
				c();
			}
		}
	});
	function c() {
		$
				.ajax({
					type : "get",
					url : "/messagePush/retriveMission?missionKey=" + d,
					dataType : "json",
					success : function(o, p) {
						var n = parseInt(o.currentCount / o.missionAmount * 100)
								+ "%", m = "";
						if (o.currentCount == o.missionAmount) {
							clearTimeout(m);
							f.css("width", "100%");
							k.html("100%");
							b.show();
							b.click(function() {
								a.removeAttr("disabled").css({
									background : "",
									color : ""
								});
								l.removeAttr("disabled");
								g.removeAttr("disabled");
								b.hide();
								h.hide();
								window.location.reload();
							});
						} else {
							f.css("width", n);
							k.html(n);
							m = setTimeout(c, 300);
						}
					}
				});
	}
}

$(document).ready(function() {
	$('.delete_broadcast').click(function() {
		if (confirm("are you sure delete?")) {
			var user_id = $(this).attr("user_id");
			var broadcast_id = $(this).attr("broadcast_id");
			$.ajax({
				url : "/broadcast/delete",
				type : "post",
				data : {
					userId : user_id,
					broadcastId : broadcast_id
				},
				cached : false,
				success : function() {
					window.location.reload();
				}
			});
		}
	});
});