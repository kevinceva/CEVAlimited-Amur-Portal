//Author : Sravan
//mailto : bevarasravan@gmail.com

function buildtablebody(jsonArray,finalstr,divid)
	{
		//alert('hai');
		var arr = finalstr.split(',');
		var len = arr.length;
		//console.log(arr.length);
		var i=0;
		var htmlString="";
		
		$('#'+divid).empty();
		if ( jsonArray.length == 0 ) {
		
			htmlString = htmlString + "<tr class='values' id="+i+">";
			htmlString = htmlString + "<td colspan="+len+" align='center'><font color='red'><strong>No Search Results Found</strong></font></td>";
			htmlString = htmlString + "</tr>";
		
		}else
		{
		
			$.each(jsonArray, function(index,jsonObject){
				++i;
				//console.log();
				htmlString = htmlString + "<tr class='values' id="+i+">";
				
				$.each(arr,function(index,key){
				
				//console.log('key ['+key+']');
				
				//alert(i);
					
				key=key.trim();
				
				if(key=='radio')
				{
					if(i==1)
						htmlString = htmlString + "<td> <input type='radio' class='genradio"+divid+"' name='genradio"+divid+"' id='genradio"+ i + "' value="+i+" checked></td>";
					else
						htmlString = htmlString + "<td> <input type='radio' class='genradio"+divid+"' name='genradio"+divid+"' id='genradio"+ i + "' value="+i+"></td>"; 
				}else if(key=='checkbox')
				{
					htmlString = htmlString + "<td> <input type='checkbox' class='gencheckbox"+divid+"' name='gencheckbox"+divid+"' id='gencheckbox"+ i + "' value="+i+"></td>"; 
					//htmlString = htmlString + "<td> <input type='checkbox' class='gencheckbox' name='gencheckbox"+divid+"' id='gencheckbox"+ i + "' value="+i+"></td>"; 
					
				}else if (key.toLowerCase().indexOf("anchorstatic:") >= 0)
				{
					
					var statickey='';
					var statval='';
					try {
						statickey=key.split(':')[1];
						statval=(key.split(':')[2]==undefined)?'':key.split(':')[2];
					}
					catch(err) {
						console.log('Exception Happen');
						statickey='';
						statval='';
					}
					
					var finalkey=statickey+i;
					var myval=read_prop(jsonObject,statickey); 
					
					// console.log(">>>>>>>>>>>>>>>>>>>> myval ["+myval+"] statval ["+statval+"]");
					htmlString = htmlString + "<td id="+finalkey+" ><a href='#' class='genaricancor"+divid+"' id="+i+" name="+i+"> " + statval + "</a></td>"; 
					
				}else if (key.toLowerCase().indexOf("anchordynamic:") >= 0)
				{

					console.log('anchordynamic');
					var statickey='';
					statickey=key.split(':')[1];
					var finalkey=statickey+i;
					var myval=read_prop(jsonObject,statickey); 	
					htmlString = htmlString + "<td id="+finalkey+" ><a href='#' class='genaricancor"+divid+"' id="+i+" name="+i+"> " + myval + "</a></td>"; 
					
				}
				else
				{	
					
					// console.log('key ['+key+']');
					
					if ((key == undefined) || (key == null) || (key == 'undefined')){
					    
						console.log("key is undefined");
					    
					}else
					{
						try
						{
							
							if(jsonObject.hasOwnProperty(key)) {
							
							//alert(jsonObject[key]);
							var myval=read_prop(jsonObject,key); //jsonObject[key]
								
								// console.log('['+key+'] Key is there value ['+myval+']');
								var finalkey=key+i;
								htmlString = htmlString + "<td id="+finalkey+" name="+finalkey+" >" + myval + "</td>";
								
							}else
							{
								// console.log('Key is not there ['+key+']');
								var finalkey=key+i;
								htmlString = htmlString + "<td id="+finalkey+" name="+finalkey+" ></td>";
							}
						
						}catch(e)
						{
							console.log("Exception Raisd ["+e+"]");
						}
						
					}
				}	
				
			});
				
				
				htmlString = htmlString + "</tr>";
								
			});
		
		}
		$('#'+divid).append(htmlString);
		htmlString="";
	}
	
	function buildjsonstrforanchor(uniqeId,divid,finalstr,type)
	{
		var arr = finalstr.split(',');
		// alert('hai babu');
		jsonObj = [];
		item = {};
		
		$.each(arr,function(index,key){
			console.log('welcome to array loop ['+key+']');
		
		item [key] = $("#"+key+""+uniqeId).text();
		});
		
		jsonObj.push(item);
		
		var finaldata = JSON.stringify(jsonObj);
		
		if(type=='form')
			$('#'+divid).val(finaldata);
		else
			$('#'+divid).html(finaldata);
		
	}
	
	
	function buildjsonstrforchecked(checkboxname,divid,finalstr,type)
	{
		var arr = finalstr.split(',');
		// alert('hai babu');
		jsonObj = [];
		try {
		
				$('input[name='+checkboxname+']:checked').each(function() {
				console.log(this.value);
				var uniqeId=this.value;
				item = {};
				$.each(arr,function(index,key){
				//console.log('welcome to array loop ['+(key+uniqeId)+'] value ['+$("#"+key+""+uniqeId).text()+']');

				item [key] = $("#"+key+""+uniqeId).text();

				});
				console.log('item '+item);
				jsonObj.push(item);

				});

				var finaldata = JSON.stringify(jsonObj);

				if(type=='form')
				$('#'+divid).val(finaldata);
				else
				$('#'+divid).html(finaldata);
				
		
		} catch (e) {
			console.log("Invalied Json Object"+e);
		}
		
		return finaldata;
	}
	
	
	function buildjsonstrforchkbxJson(checkboxname,divid,json,type)
	{
	
		try {
		
				//var finaljsonarr = jQuery.parseJSON(json);
				//var finaljsonarr = divid;
			
				var finaljsonarr = json;
			
				jsonObj = [];

				$('input[name='+checkboxname+']:checked').each(function() {
				//console.log(this.value);
				var uniqeId= parseInt(this.value)-1;
				var jsn = finaljsonarr[uniqeId];
				
				console.log("uniqeId :"+uniqeId);

				jsonObj.push(jsn);
				});

				var finaldata = JSON.stringify(jsonObj);
				console.log("finaldata ["+finaldata+"]");

				if(type=='form')
				$('#'+divid).val(finaldata);
				else
				$('#'+divid).html(finaldata);
			
		
		} catch (e) {
			console.log("Invalied Json Object"+e);
		}

		return finaldata;
		
	}
	
	
	function buildtablebodywithhidden(jsonArray,finalstr,divid)
	{
		//alert('hai');
		try{
		
		var arr = finalstr.split(',');
		var len = arr.length;
		//console.log(arr.length);
		var i=0;
		var htmlString="";
		
		$('#'+divid).empty();
		if ( jsonArray.length == 0 ) {
		
			htmlString = htmlString + "<tr class='values' id="+i+">";
			htmlString = htmlString + "<td colspan="+len+" align='center'><font color='red'><strong>No Search Results Found</strong></font></td>";
			htmlString = htmlString + "</tr>";
		
		}else
		{
		
			$.each(jsonArray, function(index,jsonObject){
				++i;
				//console.log();
				htmlString = htmlString + "<tr class='values' id="+i+">";
				
				$.each(arr,function(index,key){
				
				//console.log('key ['+key+']');
				
				//alert(i);
				
				if(key=='radio')
				{
					if(i==1)
						htmlString = htmlString + "<td> <input type='radio' class='genradio"+divid+"' name='genradio"+divid+"' id='genradio"+ i + "' value="+i+" checked></td>";
					else
						htmlString = htmlString + "<td> <input type='radio' class='genradio"+divid+"' name='genradio"+divid+"' id='genradio"+ i + "' value="+i+"></td>"; 
				}else if(key=='checkbox')
				{
					htmlString = htmlString + "<td> <input type='checkbox' class='gencheckbox"+divid+"' name='gencheckbox"+divid+"' id='gencheckbox"+ i + "' value="+i+"></td>"; 
					//htmlString = htmlString + "<td> <input type='checkbox' class='gencheckbox' name='gencheckbox"+divid+"' id='gencheckbox"+ i + "' value="+i+"></td>"; 
					
				}else if (key.toLowerCase().indexOf("anchorstatic:") >= 0)
				{
					
					var statickey='';
					var statval='';
					try {
						statickey=key.split(':')[1];
						statval=(key.split(':')[2]==undefined)?'':key.split(':')[2];
					}
					catch(err) {
						console.log('Exception Happen');
						statickey='';
						statval='';
					}
					
					var finalkey=statickey+i;
					var myval=read_prop(jsonObject,statickey); 
					
					// console.log(">>>>>>>>>>>>>>>>>>>> myval ["+myval+"] statval ["+statval+"]");
					htmlString = htmlString + "<td id="+finalkey+" ><a href='#' class='genaricancor"+divid+"' id="+i+" name="+i+"> " + statval + "</a></td>"; 
					
				}else if (key.toLowerCase().indexOf("anchordynamic:") >= 0)
				{

					//console.log('anchordynamic');
					var statickey='';
					statickey=key.split(':')[1];
					var finalkey=statickey+i;
					var myval=read_prop(jsonObject,statickey); 	
					htmlString = htmlString + "<td id="+finalkey+" ><a href='#' class='genaricancor"+divid+"' id="+i+" name="+i+"> " + myval + "</a></td>"; 
					
				}
				else
				{		
					//console.log('Come to normal thing ['+key+']');				
									
					//alert(jsonObject[key]);
					//alert(key);
					var myarr = key.split("#");
					//alert(">>>>>>>>>");
					//console.log('babu');
					var mylen = myarr.length;
					
					//console.log('mylen '+mylen);
						
						if(mylen==2)
						{
							if(jsonObject.hasOwnProperty(myarr[0]))
							{
								
								var myval=read_prop(jsonObject,myarr[0]); //jsonObject[key]
								//console.log('['+key+'] Key is there value ['+myval+']');
								var finalkey=myarr[0]+i;
								console.log('myarr[1] ['+myarr[1]+'] myarr[0] ['+myarr[0]+']');
								if(myarr[1]=='table')
									htmlString = htmlString + "<td id="+finalkey+" name="+finalkey+" >" + myval + "</td>";						
								else if(myarr[1]=='text')
									htmlString = htmlString + "<input type='text' name="+finalkey+" id="+finalkey+" value='"+ myval + "'>";
								else if(myarr[1]=='hidden') 
									htmlString = htmlString + "<input type='hidden' name="+finalkey+" id="+finalkey+" value='"+ myval + "'>"
								
							}else
							{
								//console.log('Key is not there ['+key+']');
								var finalkey=myarr[0]+i;
								htmlString = htmlString + "<td id="+finalkey+" name="+finalkey+" ></td>";
							}
						
						}
				}	
				
			});
				
				htmlString = htmlString + "</tr>";
								
			});
		
		}
		$('#'+divid).append(htmlString);
		htmlString="";
		} catch (e) {
			console.log("Invalied Json Object"+e);
		}
	}
	
	
	function buildjsonstrforcheckedwithhidden(checkboxname,divid,finalstr,type)
	{
		var arr = finalstr.split(',');
		// alert('hai babu');
		jsonObj = [];
		try {
		
				$('input[name='+checkboxname+']:checked').each(function() {
				//console.log(this.value);
				var uniqeId=this.value;
				item = {};
				$.each(arr,function(index,key){
				//console.log('welcome to array loop ['+(key+uniqeId)+'] value ['+$("#"+key[0]+""+uniqeId).text()+']');

				var myarr = key.split("#");
				var mylen = myarr.length;
				if(mylen==2)
				{
					var newkey=myarr[0];
					var newkeytype=myarr[1];
					// console.log(key);
					// console.log('newkey ['+newkey+'] newkeytype ['+newkeytype+']');
					
					if(newkeytype=="table")
						item [newkey] = $("#"+newkey+""+uniqeId).text();
					else
						item [newkey] = $("#"+newkey+""+uniqeId).val();
						
				}

				});
				//console.log('item '+item);
				jsonObj.push(item);

				});

				var finaldata = JSON.stringify(jsonObj);

				if(type=='form')
				$('#'+divid).val(finaldata);
				else
				$('#'+divid).html(finaldata);
				
		
		} catch (e) {
			console.log("Invalied Json Object>>>>"+e);
		}
		
		return finaldata;
	}
	
	function getAlldata(divid,finalstr,type)
	{
	
		var arr = finalstr.split(',');
	
		jsonObj = [];
		try {
			
				item = {};
				$.each(arr,function(index,key){
				console.log('Key is ['+key+']');

				var myarr = key.split("#");
				var mylen = myarr.length;
					if(mylen==2)
					{
						var newkey=myarr[0];
						var newkeytype=myarr[1];
						// console.log(key);
						// console.log('newkey ['+newkey+'] newkeytype ['+newkeytype+']');
						
						if(newkeytype=="table")
						{
							//console.log('table    >>> newkey ['+newkey+'] value ['+($("#"+newkey).text())+']');
							item [newkey] = $("#"+newkey).text();
						}
						else
						{
							//console.log('text    >>> newkey ['+newkey+'] value ['+($("#"+newkey).val())+']');
							item [newkey] = $("#"+newkey).val();
						}
						
							
					}

				});
				//console.log('item '+item);
				//jsonObj.push(item);

				//var finaldata = JSON.stringify(jsonObj);

				var finaldata = JSON.stringify(item);
				
				if(type=='form')
				$('#'+divid).val(finaldata);
				else
				$('#'+divid).html(finaldata);
				
		
		} catch (e) {
			console.log("Invalied Json Object>>>>"+e);
		}
		
		return finaldata;
	}
	
	function read_prop(obj, prop) {
		return obj[prop];
	}
	
	function makeemptyselectbox(selectboxid)
	{
		$("#"+selectboxid+" > option").each(function() {
			//alert(this.text + ' ' + this.value);
			if(this.value!="")
				   $(this).remove();
		});
	}
	