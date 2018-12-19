#!$HOME/.rbenv/shims/ruby
# 2018Dec14 Brockman - Updated to run the localhost validation

path = ARGV[0].to_s+""
if ( ! path ) then
  raise "Usage: validateAllIwp.rb [path]"
end
path.chop! if path[-1]=='/'

puts "# This script is designed to be piped to bash"
puts "# Scanning all .iwp files in path: #{path}"

files = Dir[ "#{path}/*.iwp" ]

files.map { |file| 
  # puts "file: #{file}"

  json = "#{file}.json"

  # build the URL
  # https://www.iwphys.org/xtoj.php/iwp-packaged/Oscillations/damped-1.iwp

  url = "http://localhost:8470/validation/#{file}?format=csv"

  puts "wget -qO- '#{url}'"

}
