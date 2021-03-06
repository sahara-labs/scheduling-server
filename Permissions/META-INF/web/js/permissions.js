/**
 * SAHARA Labs Scheduling Server
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names
 *    of its contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Michael Diponio (mdiponio)
 * @date 21st October 2011
 */

function filterClass() 
{
	var search = $(this).val();
	
	if (search.length == 0)
	{
		/* Reshow all the hidden permissions. */
		$("#userclasslist li").show();
		return;
	}
	
	$("#userclasslist > li").each(function() {
		var id = $(this).attr("id").split("_").join(" ");

		if (id.indexOf(search) == -1)
		{
			if ($(this).css("display") != "none") $(this).hide();
		}
		else 
		{
			if ($(this).css("display") == "none") $(this).show();
		}
	});
}

function createClass()
{
	$("body").append(
		"<div id='perm-createclassdialog' title='Add a new class' class='saharaform'>" +
			"<form>" +
				"<div>" +
					"<label for='newClassName'>Name:</label>" +
					"<input type='text' id='newClassName' class='validate[required]' />" +
				"</div>" +
				"<div>" +
					"<label for='newClassActive'>Active:</label>" +
					"<input type='checkbox' id='newClassActive' />" +
				"</div>" +
				"<div>" +
					"<label for='newClassQueuable'>Queuing:</label>" +
					"<input type='checkbox' id='newClassQueuable' />" +
				"</div>" +
				"<div>" +
					"<label for='newClassBookable'>Reservations:</label>" +
					"<input type='checkbox' id='newClassBookable' />" +
				"</div>" +
				"<div>" +
					"<label for='newClassPriority'>Priority:</label>" +
					"<input type='text' id='newClassPriority' value='1' class='validate[required,custom[integer],min[1]],max[255]' />" +
				"</div>" +
				"<div>" +
					"<label for='newClassTimeHorizon'>Time Horizon:</label>" +
					"<input type='text' id='newClassTimeHorizon' value='0' class='validate[required,custom[integer],min[0]]' />" +
				"</div>" +
			"</form>" +
		"</div>"
	);
	
	$("#perm-createclassdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: false,
		buttons: {
			'Create': saveCreateClass,
			'Cancel': function() {
				$(this).dialog('close');
			}
		},
		close: function() {
			$(this).dialog('destroy');
			$(".formError").remove();
			$(this).remove();
			
		},
		width: 400
	})
	.children("form").validationEngine();
	
	$("#perm-createclassdialog input")
		.focusin(formFocusIn)
		.focusout(formFocusOut);
}

function saveCreateClass() 
{
	if ($("#perm-createclassdialog form").validationEngine("validate"))
	{
		/* Tear down dialog. */
		$(this).parent().children(".ui-dialog-titlebar, .ui-dialog-buttonpane").hide();
		
		$(this).children("form").hide();
		$(this)
			.append(
				"<div class='perm-formspinner'>" +
					"<img src='/img/spinner.gif' alt='Loading...' />" +
					"<br /><br />" +
					"Adding..." +
				"</div>"
		);
		
		/* Save form. */
		$.post(
			"/permissions/addClass",
			{
				name: $("#newClassName").val(),
				active: $("#newClassActive:checked").length == 1,
				queue: $("#newClassQueuable:checked").length == 1,
				bookable: $("#newClassBookable:checked").length == 1,
				priority: $("#newClassPriority").val(),
				horizon: $("#newClassTimeHorizon").val()
			},
			function(response) {
				if (typeof response != "object" || response.wasSuccessful)
				{
					window.location.reload();
				}
				else
				{
					/* Some error occurred. */
					$diag = $("#perm-createclassdialog");
					$diag.parent().children(".ui-dialog-titlebar, .ui-dialog-buttonpane").show();
					
					$diag.children(".perm-formspinner").remove();
					$diag.children("form").show()
						.prepend(
							"<div class='ui-state ui-state-error ui-corner-all'>" +
								"<span class='perm-uiicon ui-icon ui-icon-alert'></span>" +
								response.reason +
							"</div>"
						);
				}
			}
		);
	}
}

var userClassMode = [];
function editClass() 
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");
	
	if (!userClassMode[id])
	{
		userClassMode[id] = true;
	
		$li.find("input").removeAttr("disabled");
		$li.find(".perm-editclass").empty().append(
			"<img alt='Save' src='/img/perm-save.png' />" +
			"<br />" +
			"Save"
		);
		
		$li.find(".perm-deleteclass").empty().append(
				"<img alt='Cancel' src='/img/perm-cancel.png' />" +
				"<br />" +
				"Cancel"
		);
	}
	else
	{
		if (!$li.find(".perm-userclassfields form").validationEngine("validate")) return;
		
		$.post(
				"/permissions/updateClass",
				{
					name: id,
					active: $li.find(".isActive:checked").length == 1,
					queue: $li.find(".isQueuable:checked").length == 1,
					bookable: $li.find(".isBookable:checked").length == 1,
					priority: $li.find(".getPriority").val(),
					horizon: $li.find(".getTimeHorizon").val()
				},
				function(response) {
					if (typeof response != "object") 
					{
						window.location.reload();
					}
					else
					{
						userClassMode[id] = false;
						
						$li.find('input[type!="hidden"]').attr("disabled", "disabled").each(function() {						
							if ($(this).attr("type") == "checkbox")
							{
								if ($(this).is(":checked"))
								{
									$("#" + $(this).attr("id") + "-saved").val("true")
								}
								else
								{
									$("#" + $(this).attr("id") + "-saved").val("false")
								}
							}
							else
							{
								$("#" + $(this).attr("id") + "-saved").val($(this).val());
							}
						});
						
						$li.find(".perm-editclass").empty().append(
							"<img alt='Edit' src='/img/perm-edit.png' />" +
							"<br />" +
							"Edit"
						);
						
						$li.find(".perm-deleteclass").empty().append(
								"<img alt='Delete' src='/img/perm-delete.png' />" +
								"<br />" +
								"Delete"
						);
						
						if ($li.find(".isActive:checked").length == 1)
						{
							$li.children("a").removeClass("inactiveclass").addClass("activeclass");
						}
						else
						{
							$li.children("a").removeClass("activeclass").addClass("inactiveclass");
						}
					}
				}
			);
	}
}

function cancelEditClass($li)
{
	$li.find(".perm-userclassfields form").validationEngine("hideAll");

	/* Restore the old values. */
	$li.find('.perm-userclassfields input[type!="hidden"]').attr("disabled", "disabled").each(function() {
		if ($(this).attr("type") == "checkbox")
		{
			if ($("#" + $(this).attr("id") + "-saved").val() == "true")
			{
				$(this).attr("checked", "checked");
			}
			else
			{
				$(this).removeAttr("checked");
			}
		}
		else
		{
			$(this).val($("#" + $(this).attr("id") + "-saved").val());
		}
	});

	$li.find(".perm-editclass").empty().append(
			"<img alt='Edit' src='/img/perm-edit.png' />" +
			"<br />" +
			"Edit"
	);

	$li.find(".perm-deleteclass").empty().append(
			"<img alt='Delete' src='/img/perm-delete.png' />" +
			"<br />" +
			"Delete"
	);
	
}

function deleteClass()
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");
	
	if (!userClassMode[id])
	{
		$("body").append(
				"<div id='perm-deletedialog' title='Delete user class'>" +
					"<div class='ui-priority-primary'>" +
						"Are you sure you want to delete the class '" + id.split("_").join(" ") + "'?" +
					"</div>" +
					"<div class='ui-priority-secondary'>" +
						"<span class='ui-icon ui-icon-alert'></span>" +
						"This will delete the user class as well as all the permissions in the user class and " +
						"associations with users." +
					"</div>" +
				"</div>"
			);
			
			$("#perm-deletedialog").dialog({
				resizable: false,
				modal: true,
				closeOnEscape: false,
				buttons: {
					'Delete': function() {
						$.post(
							"/permissions/deleteClass",
							{ name: id },
							function (resp) {
								if (typeof resp != "object")
								{
									window.location.reload();
								}
								else
								{
									if (resp.wasSuccessful)
									{
										$li.remove();
										$("#perm-deletedialog").dialog("close");
									}
									else
									{
										$("#perm-deletedialog").prepend(
											"<div class='ui-state-error ui-corner-all' style='padding:5px;margin-bottom:10px'>" +
												"<span class='ui-icon ui-icon-alert'></span> Unable to delete class: " +
												resp.reason + 
											"</div>"
										);
									}
								}
							}
						);	
					},
					'Cancel': function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$(this).dialog('destroy');
					$(this).remove();
					
				},
				width: 400
			});
	}
	else
	{
		userClassMode[id] = false;
		
		cancelEditClass($li);
	}
}

function addPermission() 
{
	drawPermissionDialog("Add Permission", $(this).parents("li").attr("id"));
}

function loadPermission() 
{
	var pid = $(this).attr('id').substr("perm-".length);
	
	drawPermissionDialog("Edit Permission: " + pid, $(this).parents("li").attr("id"));
	
	$("#permissiondialog form").append(
		"<input type='hidden' id='permissionId' value='" + pid + "' />"
	);
	
	$.post(
		"/permissions/getPermission",
		{ id: pid },
		function (resp) {
			var i;
			
			loadResources(resp.Type, resp.Resource);
			for (i in resp)
			{
				if (i == "UseActivityDectection")
				{
					if (resp[i])
					{
						$("#permissionUseActivityDetection").attr("checked", "checked");
					}
					else
					{
						$("#permissionUseActivityDetection").removeAttr("checked");
					}
				}
				else if (i == "Resource")
				{
					/* Do nothing. */
				}
				else
				{
					$("#permission" + i).val(resp[i]);
				}
			}
		}
	);
	
	$("#permissiondialog").dialog("option", "buttons", {
		'Save': savePermission,
		'Delete': deletePermission,
		'Close': function() { $(this).dialog("close"); }
	});
}

function drawPermissionDialog(title, userClass)
{
	var html =
		"<div id='permissiondialog' title='"+ title+ "'>" +
			"<form class='saharaform'>" +
							
				"<div id='permissiondialog-left' class='perm-col'>" + 

					"<div class='perm-header-break'><h3>Presentation</h3></div>" + 
					"<div>" +
						"<label for='permissionDisplayName'>Display name:</label>" +
						"<input type='text' id='permissionDisplayName' />" +
					"</div>" +
				
					/* Timeperiod. */
					"<div class='perm-header-break'><h3>Time Period</h3></div>" + 
					"<div class='timeline'>" +
						"<label for='permissionStartTime'>Start:</label>" +
						"<input type='text' id='permissionStartTime' class='periodcal validate[required]' />" +
						"<a id='startcalopen' class='perm-button calopen ui-corner-all'>" +
							"<img src='/img/daypicker.png' alt='Open' />" +
						"</a>" +
					"</div>" + 
					"<div class='timeline'>" +
						"<label for='permissionExpiryTime'>Expiry:</label>" +
						"<input type='text' id='permissionExpiryTime' class='periodcal validate[required]' />" +
						"<a id='expirycalopen' class='perm-button calopen ui-corner-all'>" +
							"<img src='/img/daypicker.png' alt='Open' />" +
						"</a>" +
					"</div>" + 
					
					/* Resource. */
					"<div class='perm-header-break'><h3>Resources</h3></div>" +
					"<div>" + 
						"<label for='permissionType'>Resource type:</label>" +
						"<select id='permissionType' class=;'validate[required]'>" +
							"<option value=''>&nbsp;</option>" +
							"<option value='Rig Type'>Rig Type</option>" +
							"<option value='Rig'>Rig</option>" +
							"<option value='Capabilities'>Capabilities</option>" +
						"</select>" +
					"</div>" +
					"<div>" + 
						"<label for='permissionResource'>Resource:</label>" +
						"<select id='permissionResource' class=;'validate[required]'>" +
							"<option value=''>&nbsp;</option>" +
						"</select>" +
					"</div>" +
					
				"</div>" +
				"<div id='permissiondialog-right' class='perm-col'>" + 
				
					/* Reservation. */
					"<div class='perm-header-break'><h3>Reservations</h3></div>" + 
					"<div>" +
						"<label for='permissionMaximumBookings'>Maximum concurrent reservations:</label>" +
						"<input type='text' id='permissionMaximumBookings' value='3' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
				
					/* Session timing. */
					"<div class='perm-header-break'><h3>Session Timings</h3></div>" + 
					"<div>" +
						"<label for='permissionSessionDuration'>Session duration:</label>" +
						"<input type='text' id='permissionSessionDuration' value='1800' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionAllowedExtensions'>Allowed extensions:</label>" +
						"<input type='text' id='permissionAllowedExtensions' value='2' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionExtensionDuration'>Extension duration:</label>" +
						"<input type='text' id='permissionExtensionDuration' value='900' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					
					
					/* Timeouts. */
					"<div class='perm-header-break'><h3>Timeouts</h3></div>" + 
					"<div>" +
						"<label for='permissionQueueActivityTimeout'>Queue timeout:</label>" +
						"<input type='text' id='permissionQueueActivityTimeout' value='180' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionUseActivityDetection'>Use no activity timeout:</label>" +
						"<input type='checkbox' id='permissionUseActivityDetection' checked='checked' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionSessionDetectionTimeout'>No Activity timeout:</label>" +
						"<input type='text' id='permissionSessionDetectionTimeout' value='600' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					
				"</div>" +
				"<div style='clear:both'></div>" +
				
				"<input type='hidden' id='permissionClass' value='" + userClass + "' />" +
				
			"</form>" + 
		"</div>";
	
	$("body").append(html);

	$("#permissiondialog").dialog({
		modal: true,
		width: 715,
		resizable: false,
		buttons: {
			'Save': savePermission,
			'Close': function() { $(this).dialog("close"); }
		},
		close: function() {
			$(this).children("form").validationEngine("hideAll");
			$(this).dialog("destroy").remove();
		}
		
	})
	.children("form").validationEngine();
	
	$("#permissiondialog input, #permissiondialog select")
		.focusin(formFocusIn)
		.focusout(formFocusOut);
	
	$(".periodcal").datetimepicker({
		dateFormat: "dd/mm/yy",
		showOn: "focus"
	});
	
	$("#startcalopen, #expirycalopen").click(function() {
		$(this).datetimepicker("show");
	});	
	
	$("#permissionType").change(function(val) {
		loadResources($(this).val());
	});
}

function savePermission() {
	
	if (!$("#permissiondialog form").validationEngine("validate"))
	{
		/* Failed validation. */
		return;
	}
	
	var params = { }, isUpdate = false;
	
	$('#permissiondialog input:not([type="checkbox"]), #permissiondialog select').each(function(i, e) {
		params[$(e).attr("id").substr(10)] = $(e).val();
	});
	params['UseActivityDectection'] = $("#permissionUseActivityDetection:checked").length === 1;
	
	if ($("#permissionId").length == 0)
	{
		/* Adding new permission. */
		target = "addPermission";
	}
	else
	{
		/* Saving existing permission. */
		target = "savePermission";
		isUpdate = true;
	}
	
	$.post(
		"/permissions/" + target,
		params,
		function(resp) {
			if (typeof resp != "object")
			{
				window.location.reload();
				return;
			}
			
			var name = $("#permissionDisplayName").val();
			if (resp.success && isUpdate)
			{
				$("#perm-" + $("#permissionId").val()).empty().append(
						 (name == "" ? $("#permissionType").val() + ": " +
									$("#permissionResource").val().split(" ").join(" ") : name)
				);
			}
			else if (resp.success)
			{
				var clazz = $("#permissionClass").val(),
						permList = $("#" + clazz + " .perm-permissionlist"), html = "";
				
				if (permList.length == 0)
				{
					/* Add permission list. */
					$("#" + clazz + " .perm-userclasspermissions").empty().append(
						"<ul class='perm-permissionlist'></ul>"
					);
					
					permList = $("#" + clazz + " .perm-permissionlist");
				}
			
				permList.append(
						"<li id='perm-" + resp.id + "'>" + (name == "" ? $("#permissionType").val() + ": " +
						$("#permissionResource").val().split("_").join(" ") : name) + "</li>");
				$("#perm-" + resp.id).click(loadPermission);
			}
			
			$("#permissiondialog").dialog("close");
		}
	);
}

function deletePermission()
{
	$("#permissiondialog").prepend(
		"<div id='permissionoverlay'></div>" +
		"<div id='permissionconfirm'>" +
			"<div>Are you sure you want to delete this permission?</div>" +
			"<div class='permissionconfirmbuttons'>" +
				"<button id='permissionconfirmcancel'>Cancel</button>" +
				"<button id='permissionconfirmdelete'>Delete</button>" +
			"</div>" +
		"</div>"
	);
	
	$("#permissionconfirmdelete, #permissionconfirmcancel").button();
	
	$("#permissionconfirmcancel").click(function() {
		$("#permissionoverlay, #permissionconfirm").remove();
	});
	
	$("#permissionconfirmdelete").click(function() {
		$.post(
			"/permissions/deletePermission",
			{ "pid": $("#permissionId").val() },
			function(resp) {
				if (resp.success)
				{
					var $li = $("#perm-" + $("#permissionId").val()), $ul = $li.parent();
					$li.remove();
					
					if ($ul.children().length == 0)
					{
						var $dv = $ul.parent();
						$ul.remove();
						
						$dv.append(
							'<div class="ui-state ui-state-highlight ui-corner-all">' +
    							'<span class="ui-icon ui-icon-info"></span>' +
    							'No permissions.' +
    						'</div>'
						);
					}
					
					$("#permissiondialog").dialog("close");
				}
				else
				{
					$("#permissionconfirm").prepend(
						"<div class='ui-state ui-state-error'>" +
							"<span class='ui-icon ui-icon-alert'></span>" + 
							"Failed to delete permission: " + resp.reason +
						"</div>"
					);
				}
			}
		);
	});
}

function loadResources(type, valset)
{
	var mtype = type.split(' ').join('').toUpperCase();
	
	$.post(
		"/permissions/loadResources",
		{ "type": mtype },
		function (ret) {
			var html = ""; 
			
			for (i in ret)
			{
				html += "<option value='" + ret[i] + "'>" + ret[i] + "</option>";
			}
			
			$("#permissionResource").empty().append(html)
				.prev().empty().append(type + ":");
			
			if (valset)
			{
				$("#permissionResource").val(valset);
			}
		}
	);
}

function addUserToClass()
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");

	$("body").append(
			"<div id='perm-adduserdialog' title='Add User to Group: " + id.split("_").join(" ") + "'>" +
				"<div class='perm-autocomplete saharaform'>" +
					"<label for='perm-acinput'>Search:&nbsp;&nbsp;</label>" +
					"<input id='perm-acinput' type='text' />" +
				"</div>" + 
				
				"<div id='perm-acuserslist'>" +
				"</div>" + 
				
				"<div id='perm-selectedusers'>" +
					"<p>Users to add:</p>" +
					"<ul> </ul>" +
				"</div>" +
				
				"<div style='clear:both'> </div>" +
			"</div>"
	);

	$("#perm-adduserdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 600,
		buttons: {
			'Add': function() {
				var addUsers = [];
				$("#perm-selectedusers input").each(function() {
					var id = $(this).attr('id');
					for (i in idsList)
					{
						if (idsList[i] == id)
						{
							addUsers.push(i);
							break;
						}
					}
				});
				
				if (addUsers.length > 0)
				{
					$.post(
						"/users/addUsersToClass",
						{
							users: addUsers.toString(),
							name: id
						},
						function(resp) {
							if (typeof resp != "object") window.location.reload();
							else ($("#perm-selectedusers ul").empty());
						}
					);
				}
			},
			'Close': function() { $(this).dialog("close") }
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();
		},
	});
	
	var lastSearch = "", idsList = [];
	
	$("#perm-acinput").keyup(function () {
		var search = $(this).val();
		
		if (search.length == lastSearch.length) return;
		lastSearch = search;
		
		if (search.length == 0) 
		{
			$("#perm-acuserslist").empty();
			return;
		}
		
		$.post(
			"/users/list",
			{ 
				search: search,
				notIn: id,
				max: 20
			},
			function (resp)
			{
				if (typeof resp != "object") 
				{
					window.location.reload();
					return;
				}

				var i = -1, html = "", len = Math.ceil(resp.length / 3), u;
				for (i in resp)
				{
					if (i > 0 && i % len == 0) html += "</ul></div>";
					if (i % len == 0) html += "<div class='perm-userscol'><ul>";
					
					u = resp[i];
					idsList[u.name] = "user-" + u.name.replace(/\.|\s|,/, "");
					html +=
						"<li>" +
							"<input type='checkbox' id='" + idsList[u.name] + "' />" +
							"<label for='" + idsList[u.name] + "'>" + u.display + "</label>" + 
						"</li>";
				}
				if (i != -1) html += "</ul></div>";

				$("#perm-acuserslist").empty().append(html);
				
				$("#perm-acuserslist input").change(function() {
					
					if ($(this).is(":checked"))
					{
						$(this).parent().detach().appendTo("#perm-selectedusers ul")
					}
					else
					{
						$(this).parent().remove();
					}
				});
			}
		);
		
	})
	.focusin(formFocusIn)
	.focusout(formFocusOut);
}


function deleteUserInClass()
{
	var usersList = [], idsList = [], oldSearch = "";
	
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");
	
	/* Clear any old invocations. */
	usersList = [];
	idsList = [];
	oldSearch = "";

	$("body").append(
			"<div id='perm-deleteusersdialog' title='Remove from Group: " + id.split("_").join(" ") + "'>" +
				"<div id='perm-usersearch' class='saharaform'>" +
					"<label for='perm-usersearchinput'>Filter:&nbsp;&nbsp;</label>" +
					"<input id='perm-usersearchinput' />" +
				"</div>" +
				"<div id='perm-userslist'> </div>" +
				"<div style='clear:both'> </div>" +
			"</div>"
	);

	$("#perm-deleteusersdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 600,
		buttons: {
			'Remove': function() {
				var delUsers = [];
				
				$("#perm-deleteusersdialog input:checked").each(function() {
					var id = $(this).attr('id');
					for (i in idsList)
					{
						if (idsList[i] == id)
						{
							delUsers.push(i);
							break;
						}
					}
				});
				
				if (delUsers.length > 0)
				{
					$.post(
						"/users/deleteUsersInClass",
						{
							users: delUsers.toString(),
							name: id
						},
						function(resp) {
							if (typeof resp != "object") window.location.reload();
							else for (i in delUsers) $("#" + idsList[delUsers[i]]).parent().remove();
						}
					);
				}
			},
			'Close': function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();
		},
	});
			
	$.post(
		"/users/list",
		{ "in": id },
		function (resp) {
			if (typeof resp != "object")
			{
				window.location.reload();
				return;
			}
			
			resp.sort(function(a, b) {
				return a.display == b.display ? 0 : (a.display > b.display ? 1 : -1);
			});
			usersList = resp;
			
			var i = -1, html = "", len = Math.ceil(resp.length / 4), u;
			for (i in resp)
			{
				if (i > 0 && i % len == 0) html += "</ul></div>";
				if (i % len == 0) html += "<div class='perm-userscol'><ul>";
				
				u = resp[i];
				idsList[u.name] = "user-" + u.name.replace(/\.|\s|,|'/g, "");
				html +=
					"<li>" +
						"<input type='checkbox' id='" + idsList[u.name] + "' />" +
						"<label for='" + idsList[u.name] + "'>" + u.display + "</label>" + 
					"</li>";
			}
			if (i != -1) html += "</ul></div>";
			
			$("#perm-userslist").append(html);
		}
	);
	
	$("#perm-usersearchinput").keyup(function() {
		var i, search = $(this).val(), nid;
		
		/* This accounts for key presses that do not add to the search. */
		if (search.length == oldSearch.length) return;
		
		oldSearch = search;
		
		if (search.length == 0)
		{
			$("#perm-deleteusersdialog li").css("display", "block");
			return;
		}
		
		for (i in usersList)
		{
			nid = "#" + idsList[usersList[i].name];
			if (usersList[i].display.indexOf(search) == -1)
			{
				$(nid).parent().css("display", "none");
			}
			else
			{
				$(nid).parent().css("display", "block");
			}
		}
	})
	.focusin(formFocusIn)
	.focusout(formFocusOut);
}

function deleteAllUsersInClass()
{
	var $li = $(this).parents(".perm-userclass"), 
		id = $li.attr("id");

	$("body").append(
			"<div id='perm-removeallusersdialog' title='Remove all user associations'>" +
				"<div class='ui-priority-primary'>" +
					"Are you sure you want to delete all the user associations in the class " +
					"'" + id.split("_").join(" ") + "'?" +
				"</div>" +
				"<div class='ui-priority-secondary'>" +
					"<span class='ui-icon ui-icon-alert'></span>" +
					"Only the associations with the users will be deleted. The associated users will not be deleted " +
					"by this operation." +				
				"</div>" +
			"</div>"
	);

	$("#perm-removeallusersdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 450,
		buttons: {
			'Remove': function() {
				$.post(
						"/permissions/deleteAllUsersInClass",
						{ name: id },
						function (resp) {
							if (typeof resp != "object")
							{
								window.location.reload();
							}
							
							$("#perm-removeallusersdialog").dialog("close");
						}
				);	
			},
			'Cancel': function() {
				$(this).dialog('close');
			}
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();

		},
	});
}

function userClassKeys()
{
	var $li = $(this).parents(".perm-userclass"), 
		id = $li.attr("id"),
		committing = false;

	$("body").append(
			"<div id='perm-keysdialog' title='Access Keys'>" +
				"<div id='perm-keysactions'>" +
					"<a class='perm-keysadd perm-button'>" +
						"<img src='/img/perm-add.png' alt='Add' /><br />" +
						"Add Key" +
					"</a>" +
//					"<a class='perm-keysemail perm-button'>" +
//						"<img src='/img/perm-email.png' alt='Email' /><br />" +
//						"Email Key" +
//					"</a>" +
//					"<a class='perm-keysbulk perm-button'>" +
//						"<img src='/img/perm-bulkemail.png' alt='Bulk' /><br />" +
//						"Bulk Email" +
//					"</a>" +
				"</div>" +
				"<div id='perm-keyslist'>&nbsp;" +
					"<ul id='perm-activekeys'>" +
					"</ul>" +
				"</div>" +
				"<div style='clear:both'></div>" +
			"</div>"
	);

	$("#perm-keysdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 600,
		buttons: {
			'Close': closeDialog
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();
		},
	});
	
	$("#perm-keysdialog .perm-keysadd").click(addKey);
	$("#perm-keysdialog .perm-keysemail").click(emailKey);
	$("#perm-keysdialog .perm-keysbulk").click(bulkEmail);
	
	$.post(
		"/keys/getList",
		{ name: id },
		function(resp) {
			if (!$.isArray(resp)) 
			{
				window.location.reload();
				return;
			}
			
			if (resp.length > 0)
			{
				var i, html = ''; 
				for (i in resp) html += keyLi(resp[i]);
				$("#perm-keyslist ul").append(html);
				
				$("#perm-keyslist ul li").click(showKey);
			}
			else
			{
				$("#perm-keyslist").append(
						"<div class='ui-state-highlight ui-corner-all'>" +
							"<span class='ui-icon ui-icon-info'></span>" +
							"This user class has no access keys." +
						"</div>"
				);
			}
		}
	)
	
	function showKey()
	{
		var key = $(this).text().split(/\s/)[1];
		
		$("#perm-keysdialog").append(
				"<div id='perm-keysmodal' class='saharaform'>" +
					"<form>" +
						"<div id='perm-keydetailsdisplay' class='perm-keysmodalcol'>" +
							"<div class='keys-title'>Details:</div>" +
						"</div>" +
						"<div id='perm-redeemuserslist'>" +	
							"<div class='keys-title'>Redeemed Users:</div>" +
						"</div>" +
						"<div style='clear:both'></div>" +
					"</form>" +
				"</div>"
		).dialog("option", {
			title: "Key '" + key + "' Details",
			buttons: {
				'Discard': discardKey,
				'Back': restoreDialog,
				'Close': closeDialog
			}
		});

		$.post(
			"/keys/keyDetails",
			{
				key: key
			},
			function(resp) {
				if (typeof resp != "object")
				{
					window.location.reload();
					return;
				}
				
				var i, o, html = 
					"<table>" +
						"<tr><td class='firstcol'>Key:</td><td>" + resp.key + "</td></tr>" +
						"<tr><td class='firstcol'>Class:</td><td>" + resp.userClass.split("_").join(" ") + "</td></tr>" + 
						"<tr><td class='firstcol'>Active:</td><td>" + resp.active + "</td></tr>" + 
						"<tr><td class='firstcol'>Remaining Uses:</td><td>" + resp.remaining + "</td></tr>";
				
				if (resp.hasExpiry)
				{
					html += "<tr><td class='firstcol'>Expiry:</td><td>" + resp.expiry.substr(0, resp.expiry.length - 2) + "</td></tr>";
				}
				
				if ($.isArray(resp.constraints))
				{
					for (i in resp.constraints)
					{
						var o = resp.constraints[i];
						
						html += "<tr><td class='firstcol'>" + o.name + ":</td><td>" + o.value + "</td></tr>"; 
					}
				}
						
				html += "</table>";
				$("#perm-keydetailsdisplay").append(html);
				
				if ($.isArray(resp.user))
				{
					html = "<ul>";
					
					for (i in resp.user)
					{
						html += "<li>" + resp.user[i] + "</li>";
					}
					
					html += "</ul>";
					
					$("#perm-redeemuserslist").append(html);
				}
				else
				{
					$("#perm-redeemuserslist").append(
						"<div class='ui-state-highlight ui-corner-all'>" +
							"<span class='ui-icon ui-icon-info'></span>" +
							"No redeemed users." +
						"</div>"
					);
				}
			}
		);
		
		function discardKey()
		{
			$.post(
				"/keys/invalidateKey",
				{
					key: key
				},
				function(resp) {
					if (typeof resp == "object" && resp.success)
					{
						$("#perm-activekeys .perm-keyvalue").each(function () {
							if ($(this).text() == key) $(this).parent().remove();
							
							if ($("#perm-activekeys").children().length == 0)
							{
								$("#perm-keyslist").append(
										"<div class='ui-state-highlight ui-corner-all'>" +
											"<span class='ui-icon ui-icon-info'></span>" +
											"This user class has no access keys." +
										"</div>"
								);
							}
						});
						restoreDialog();
					}
					else window.location.reload();
				}
				
			);
		}
	}
	
	function addKey()
	{
		if ($("#perm-keysmodal").length != 0) return;
		
		$("#perm-keysdialog").append(
				"<div id='perm-keysmodal' class='saharaform'>" +
					"<form>" +
						"<div class='perm-keysmodalcol'>" +
							"<div class='keys-title'>Required Details:</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-userclass'>User Class: </label>" +
								"<input id='keys-userclass' type='text' value='" + id.split("_").join(" ") + "' disabled='disabled' />" +
							"</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-uses'>Key Uses: </label>" +
								"<input id='keys-uses' type='text' class='validate[required,custom[integer],min[1]]' value='1' />" +
							"</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-hastimelimit'>Time Limited: </label>" +
								"<input id='keys-hastimelimit' type='checkbox' />" +
							"</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-expiry'>Expiry: </label>" +
								"<div class='perm-keysformlinedis'>" +
									"<input id='keys-expiry' type='text' disabled='disabled' />" +
									"<a id='keys-expiryopen' class='perm-button calopen ui-corner-all'>" +
										"<img src='/img/daypicker.png' alt='Open' />" +
									"</a>" +
								"</div>" +
							"</div>" +
						"</div>" +
						"<div id='perm-constraints' class='perm-keysmodalcol' class='saharaform'>" +	
							"<div class='keys-title'>Constraints:</div>" +
						"</div>" +
						"<div style='clear:both'></div>" +
					"</form>" +
				"</div>"
		).dialog("option", {
			title: "Add a New Access Key",
			buttons: {
				'Add': commitAddKey,
				'Back': restoreDialog,
				'Close': closeDialog
			}
		});
		
		$("#perm-keysmodal input").focusin(formFocusIn).focusout(formFocusOut);
		$("#perm-keysmodal form").validationEngine();
		
		$("#keys-expiry").datetimepicker({
			dateFormat: "dd/mm/yy",
			showOn: "focus",
			minDate: new Date()
		});
		
		$("#keys-expiryopen").click(function() {
			if ($("#keys-hastimelimit").is(":checked")) $("#keys-expiry").datetimepicker("show");
		});
		
		$("#keys-hastimelimit").click(function() {			
			if ($(this).is(":checked"))
			{
				$("#keys-expiry")
					.removeAttr("disabled")
					.parent().removeClass("perm-keysformlinedis");
			}
			else
			{
				$("#keys-expiry")
					.attr("disabled", "disabled")
					.parent().addClass("perm-keysformlinedis");
			}
		});
		
		$.post(
			"/keys/getConstraints",
			 null,
			 function(resp) {
				if (typeof resp != "object")
				{
					window.location.reload();
					return;
				}
				
				var i, j, c, r, html =
						"<div class='perm-constraintsform'>";
				for (i in resp)
				{
					c = resp[i];

					html += 
						"<div class='perm-constraintsline'>" +
							"<label for='keys-" + c.name + "-en' class='perm-constraintslabel'>" + c.name + ":</label>" +
							"<div class='perm-constraintenable'><input id='keys-" + c.name + "-en'  type='checkbox' /></div>";
					
					if (c.hasRestriction)
					{
						html += "<select id='keys-" + c.name + "'  disabled='disabled'>" +
									"<option value=''>&nbsp;</option>";
						
						for (j in c.restriction)
						{
							r = c.restriction[j].split(/:/);
							html += "<option value='" + r[1] + "'>" + r[0] + "</option>";
						}
						
						html += "</select>";
					}
					else
					{
						html += "<input id='keys-" + c.name + "' type='text' disabled='disabled' />";
					}
					
					html +=	
						"</div>";
				}
				
				html += "</div>";
				
				$("#perm-constraints")
					.append(html)
					.find("input, select").focusin(formFocusIn).focusout(formFocusOut);
				
				$('#perm-constraints input[type="checkbox"]').click(function() {
					var $input = $(this).parent().next();
					
					if ($input.attr("disabled"))
					{
						$input.removeAttr("disabled");
					}
					else
					{
						$input.val("").attr("disabled", "disabled");
					}
				});
			 }
		);
	}
	
	function commitAddKey()
	{
		if (committing) return;
		committing = true;
		
		if (!$("#perm-keysmodal form").validationEngine("validate")) return;
		
		var params = {
				name: id,
				uses: $("#keys-uses").val(),
				timelimited: $("#keys-hastimelimit").is(":checked"),
				expiry: $("#keys-expiry").val()
		};
		
		$("#perm-constraints input[type='checkbox']:checked").each(function() {
			var $input = $(this).parent().next();
			params[$input.attr("id").substr(5)] = $input.val();
		});
		
		$.post(
				"/keys/addKey",
				params,
				function(resp) {
					if (typeof resp != "object")
					{
						window.location.reload();
						return;
					}
					
					committing = false;
					$("#perm-activekeys")
						.append(keyLi(resp))
						.children(":last").click(showKey);
						
					
					$("#perm-keyslist .ui-state-highlight").remove();
					restoreDialog();
					displayMessage("Key added.");
				}
		);
	}
	
	function emailKey()
	{
		if ($("#perm-keysmodal").length != 0) return;
		
		$("#perm-keysdialog").append(
				"<div id='perm-keysmodal' class='saharaform'>" +
					"<form>" +
						"<div class='perm-keysmodalcol'>" +
							"<div class='keys-title'>User Details:</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-firstname'>First Name: </label>" +
								"<input id='keys-firstname' type='text' class='validate[required]' />" +
							"</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-lastname'>Last Name: </label>" +
								"<input id='keys-lastname' type='text' class='validate[required]' />" +
							"</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-email'>Email: </label>" +
								"<input id='keys-email' type='text' class='validate[required,custom[email]]' />" +
							"</div>" +
							"<div id='keys-expiryline' class='perm-keysformline'>" +
								"<label for='keys-expiry'>Expiry: </label>" +
								"<input id='keys-expiry' type='text' class='validate[required]' />" +
								"<a id='keys-expiryopen' class='perm-button calopen ui-corner-all'>" +
									"<img src='/img/daypicker.png' alt='Open' />" +
								"</a>" +
							"</div>" +
						"</div>" +
						"<div class='perm-keysmodalcol'>" + 
							"<div class='perm-keysformline'>" +
								"<label for='keys-type'>Type: </label>" +
								"<select id='keys-type' class='validate[required]'>" +
									"<option value=''>&nbsp;</option>" +
								"</select>" +
							"</div>" +
							"<div class='keys-title'>(Optional) Message:</div>" +
							"<textarea id='keys-emailmessage' />" +
						"</div>" +
						"<div style='clear:both'></div>" +
					"</form>" +
				"</div>"
		).dialog("option", {
			title: "Email Access Key",
			buttons: {
				'Email': commitEmailKey,
				'Back': restoreDialog,
				'Close': closeDialog
			}
		});
		
		$.post(
			"/keys/getEmailKeyTypes",
			null,
			function(resp) {
				if (!$.isArray(resp))
				{
					window.location.reload();
					return;
				}
				
				var html = "";
				for (i in resp)
				{
					html += "<option value='" + resp[i] + "'>" + resp[i] + "</option>";
				}
				$("#keys-type").append(html);
			}
		);
		
		$("#perm-keysmodal form").validationEngine();
		$("#perm-keysmodal input, #perm-keysmodal textarea").focusin(formFocusIn).focusout(formFocusOut);
		
		$("#keys-expiry").datetimepicker({
			dateFormat: "dd/mm/yy",
			showOn: "focus",
			minDate: new Date()
		});
		
		$("#keys-expiryopen").click(function() {
			$("#keys-expiry").datetimepicker("show");
		});
	}
	
	function commitEmailKey()
	{
		if (!$("#perm-keysmodal form").validationEngine("validate")) return;
		
		$.post(
			"/keys/emailKey",
			{
				name: id,
				first: $("#keys-firstname").val(),
				last: $("#keys-lastname").val(),
				email: $("#keys-email").val(),
				message: $("#keys-emailmessage").val(),
				expiry: $("#keys-expiry").val(),
				type: $("#keys-type").val()
			},
			function (resp) {
				if (typeof resp != "object")
				{
					window.location.reload();
					return;
				}
				
				restoreDialog();
				displayMessage("Email sent.");
			}
		);
	}
	
	function bulkEmail()
	{
		if ($("#perm-keysmodal").length != 0) return;
		
		$("#perm-keysdialog").append(
				"<div id='perm-keysmodal' class='saharaform'>" +
					"<form>" +
						"<div class='perm-keysmodalcol'>" +
							"<div class='keys-title'>User Details:</div>" +
							"<div class='perm-keysformline'>" +
								"<label for='keys-userfile'>File: </label>" +
								"<input id='keys-userfile' name='userfile' type='file' class='validate[required' />" +
							"</div>" +
							"<div id='keys-expiryline' class='perm-keysformline'>" +
								"<label for='keys-expiry'>Expiry: </label>" +
								"<input id='keys-expiry' type='text' class='validate[required]' />" +
								"<a id='keys-expiryopen' class='perm-button calopen ui-corner-all'>" +
									"<img src='/img/daypicker.png' alt='Open' />" +
								"</a>" +
							"</div>" +
						"</div>" +
						"<div class='perm-keysmodalcol'>" + 
							"<div class='perm-keysformline'>" +
								"<label for='keys-type'>Type: </label>" +
								"<select id='keys-type' class='validate[required]'>" +
									"<option value=''>&nbsp;</option>" +
								"</select>" +
							"</div>" +
							"<div class='keys-title'>(Optional) Message:</div>" +
							"<textarea id='keys-emailmessage' />" +
						"</div>" +
						"<div style='clear:both'></div>" +
					"</form>" +
				"</div>"
		).dialog("option", {
			title: "Email Access Key",
			buttons: {
				'Email': commitEmailKey,
				'Back': restoreDialog,
				'Close': closeDialog
			}
		});
		
		$.post(
			"/keys/getEmailKeyTypes",
			null,
			function(resp) {
				if (!$.isArray(resp))
				{
					window.location.reload();
					return;
				}
				
				var html = "";
				for (i in resp)
				{
					html += "<option value='" + resp[i] + "'>" + resp[i] + "</option>";
				}
				$("#keys-type").append(html);
			}
		);
		
		$("#perm-keysmodal form").validationEngine();
		$("#perm-keysmodal input, #perm-keysmodal textarea").focusin(formFocusIn).focusout(formFocusOut);
		
		$("#keys-expiry").datetimepicker({
			dateFormat: "dd/mm/yy",
			showOn: "focus",
			minDate: new Date()
		});
		
		$("#keys-expiryopen").click(function() {
			$("#keys-expiry").datetimepicker("show");
		});
	}

	function restoreDialog()
	{
		$("#perm-keysmodal form").validationEngine("hideAll");
		$("#perm-keysmodal").remove();
		
		$("#perm-keysdialog").dialog("option", {
			title: "Access Keys",
			buttons: {
				'Close': closeDialog
			}
		});
	}
	
	function closeDialog()
	{
		$("#perm-keysmodal form").validationEngine("hideAll");
		$(this).dialog('close');
	}
	
	function displayMessage(message)
	{
		$("#perm-keysdialog").append(
			"<div id='perm-displaymessage'>" +
				"<span class='ui-icon ui-icon-info'></span>" +
				message + 
			"</div>"
		);
		
		setTimeout(function() {
			$("#perm-displaymessage").fadeOut(400, function() {
				$(this).remove();
			});
		}, 2000);
	}
	
	function keyLi(key)
	{
		var li = "<li>" +
					"<span class='perm-keylabel'>Key: </span>" +
					"<span class='perm-keyvalue'>" + key.key + "</span> " +
					"<span class='perm-keyremaining'>(";

		if      (key.remaining === 0) li += "no more uses";
		else if (key.remaining === 1) li += "1 use";
		else if (key.remaining > 1)   li += key.remaining + " uses";
		
		li += ")</span>" + 
			"</li>";
		
		return li;
	}
}

