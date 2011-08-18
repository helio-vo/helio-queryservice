<?xml version="1.0" encoding="UTF-8"?>

<!-- New document created with EditiX at Fri Aug 05 11:42:43 BST 2011 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tab="urn:astrogrid:schema:TableMetadata">
	<xsl:output method="html"/>
	<xsl:template match="tab:tables">
		<html>
			<body>
				<h2>VOSI Tables view</h2>
				<xsl:for-each select="table">
					<h3>
						<xsl:value-of select="name"/>
					</h3>
					<table border="1">
						<xsl:for-each select="column">
							<tr>
								<th>
									<xsl:value-of select="name"/>
								</th>
								<td>
									<xsl:value-of select="datatype"/>
								</td>
								<td>
									<xsl:value-of select="description"/>
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
