
<%@ taglib prefix="s" uri="/struts-tags" %>
  
  
    				 <table id="colTable" width="100%" border="0" cellspacing="1" cellpadding="0">
    				 		<TR height=20 class="PopupAltDataRow">
								<TD nowrap class="txtblackBP" width="15%">Time Constraint:</TD>
								<td >&nbsp;&nbsp;&nbsp;<s:textfield id="timeConstraint" name="timeConstraint" cssClass="textfield" size="20" maxlength="200" value="" theme="simple"/></td>				
							</TR>
							<TR height=20 class="PopupAltDataRow">
								<TD nowrap class="txtblackBP" >Instrument Constraint:</TD>
								<td>&nbsp;&nbsp;&nbsp;<s:textfield id="instrumentConstraint" name="instrumentConstraint" cssClass="textfield" size="20" maxlength="200" value="" theme="simple"/></td>				
							</TR>
								<TR height=20 class="PopupAltDataRow">
								<TD nowrap class="txtblackBP" >Coordinate Constraint:</TD>
								<td>&nbsp;&nbsp;&nbsp;<s:textfield id="coordinateConstraint" name="coordinateConstraint" cssClass="textfield" size="20" maxlength="200" value="" theme="simple"/></td>				
							</TR>
							</TR>
								<TR height=20 class="PopupAltDataRow">
								<TD nowrap class="txtblackBP" >Oder By Constraint:</TD>
								<td>&nbsp;&nbsp;&nbsp;<s:textfield id="orderByConstraint" name="orderByConstraint" cssClass="textfield" size="20" maxlength="200" value="" theme="simple"/></td>				
							</TR>
							</TR>
								<TR height=20 class="PopupAltDataRow">
								<TD nowrap class="txtblackBP" >Limit Constraint:</TD>
								<td>&nbsp;&nbsp;&nbsp;<s:textfield id="limitConstraint" name="limitConstraint" cssClass="textfield" size="20" maxlength="200" value="" theme="simple"/></td>				
							</TR>
					 		<%
						        int i=0;
						    %>
				        	<s:iterator value="columnTO" status="rowstatus" id="colTO">
				        	 <%
						        i++;
						     %>
					           <s:if test="#rowstatus.odd == true">
									<s:set name="classType" value="'PopupDataRow'"/>
								</s:if>
								<s:else>
									<s:set name="classType" value="'PopupAltDataRow'"/>
								</s:else>
								<tr class='<s:property value="#classType"/>' width="100%" id="colRowId<%=i%>" onclick="javascript: functionDoRowSel('<%=i%>');">
							   		<td height="20" align="center" valign="middle">										
										<s:checkbox name="sColumnName" id="sColumnName" fieldValue='%{#colTO.getColumnName()}'   theme="simple"/>
									</td>
									<td align="left" valign="middle"><s:property value="#colTO.getColumnName()"/>  </td>							
							    </tr>	
							</s:iterator>
						<tr class="bgline">
    					<td height="2" colspan="2"><img src="Images/spacer.gif" width="1" height="1"></td>
    					
				    </tr>    
				    <tr>
   					 <td>
   					    
   						 <table width="100%" border="0" cellspacing="0" cellpadding="0">
						    <tr align="left" valign="middle">
								<td align="left">&nbsp;
									<a  onclick="javascript: addColumnsOfSelectedTable()" style="cursor:hand" onMouseOver="MM_swapImage('addcol','','Images/but_addColumn_click.gif',1)" onMouseOut="MM_swapImgRestore()"><img src="Images/but_addColumn_normal.gif" name="Close"  border="0" align="absmiddle" id="addcol"></a>
								</td>
					        </tr>
					     </table>
					  
					</td>
				  </tr>		
				   </table>
					
 

