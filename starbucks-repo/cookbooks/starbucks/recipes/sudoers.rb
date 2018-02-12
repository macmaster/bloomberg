# sudoers.rb

template "#{ENV['HOME']}/sudoers.rb" do
	source 'sudoers.erb'
	mode '0440'
	owner ENV['USER']
	group ENV['USER']
	variables(
		sudoers_groups: node['authorization']['sudo']['groups'],
		sudoers_users: node['authorization']['sudo']['users']
	)
end
