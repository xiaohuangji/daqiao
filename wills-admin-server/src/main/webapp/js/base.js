$(function(){ 
/*left menu*/
	/*"use strict";*/
	var navList = ({

		init : function () {
			var bt = $('.l_nav li a'),
				ifra = $('.r_ifra iframe');

			bt.live('click', function () {
				var url = $(this).attr('hrefd');
				bt.removeClass('on');
				$(this).addClass('on');
				ifra.attr('src', url);
				return false;
			});

			return this;
		}

	}.init()),
	
	countWord = ({

		init : function(){

			this.countWords();

			return this;
		},

		countWords : function(){
			var txta = $('textarea[max]'),
				isMax = $('.txtsize');

				if( txta.length>0 ){
					txta.live('keyup',function(){
						var inNum = this.value.length,
							max = $(this).attr('max'),
							show = $(this).nextAll('.txtsize').eq(0),
							submit = $(this).closest('form').find('input[type=submit],input[submit],button[submit]');
						    show[0].style.display="block";
							if(inNum > max){
								show.addClass('red');	
							}else{
								show.removeClass('red');
							}
							show.html(inNum+"/"+max);

						if( !isMax.hasClass("red") ){
							submit.removeAttr("disabled").removeAttr("id");
						}else{
							submit.attr({"disabled":"disabled",'id':'bt_dis'});
						}

					});
				}

		}

	}.init());
});