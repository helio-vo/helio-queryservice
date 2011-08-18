<?xml version="1.0" encoding="UTF-8"?>

<!-- New document created with EditiX at Fri Aug 05 11:42:43 BST 2011 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:cap="urn:astrogrid:schema:Capabilities">
	<xsl:output method="html"/>
	<xsl:template match="cap:capabilities">
		<html>
			<body>
				<h2>VOSI Capabilities view</h2>
				<xsl:for-each select="capability">
					<h3> 
						<xsl:value-of select="@standardID"/>
					</h3>
					<table border="1">
						<tr>
							<th>Type</th>
							<th>URL</th>
							<th>Query Type</th>
							<th>Result Type</th>
						
						</tr>
						<xsl:for-each select="interface">
							<tr>
								<td>
									<xsl:value-of select="@xsi:type"/>
								</td>
								<td>
									<xsl:value-of select="accessURL"/>
								</td>
								<td>
									<xsl:value-of select="queryType"/>
								</td>
								<td>
									<xsl:value-of select="resultType"/>
								</td>
								
							</tr>
						</xsl:for-each>
					</table>
					<br/>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
